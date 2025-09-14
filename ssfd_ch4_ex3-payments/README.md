# ssfd_ch4_ex3-payments

A minimal Spring Boot service exposing a single endpoint to accept a refunds JSON file and verify its integrity using a SHA3-256 hash provided via request header.

## Run the application

Prerequisites: Java 24 (as per pom.xml), Maven wrapper included.

- Windows PowerShell:
  - .\mvnw.cmd spring-boot:run
- Linux/macOS (if applicable):
  - ./mvnw spring-boot:run

By default, the app starts on http://localhost:8080.

## Endpoint

- Method: POST
- URL: /api/refunds
- Consumes: application/json
- Produces: application/json
- Required header: X-Content-SHA3 — the SHA3-256 hex digest of the exact raw request body bytes

Notes:
- The provided hash is compared case-insensitively, but it is conventional to use lowercase hex.
- The hash must be computed from the exact bytes sent (no extra whitespace, newlines, or reformatting between hashing and sending).

## Request body format

A JSON array of refund objects:

[
  {
    "orderId": "10001",
    "amount": 120
  },
  {
    "orderId": "10002",
    "amount": 450
  }
]

Fields:
- orderId (string)
- amount (number) — parsed as BigDecimal on the server

## Computing the SHA3-256 hash (hex)

For this exercise, you must generate the SHA3-256 hash using the companion application ssfd_ch4_ex3-warehouse.

- Open and run the ssfd_ch4_ex3-warehouse project (see that project's README for exact run instructions).
- Use its provided functionality to compute the SHA3-256 of the exact refunds JSON you will send to this service.
- Copy the resulting lowercase hex digest and set it in the X-Content-SHA3 request header when calling this service.

Important:
- Ensure the file content used for hashing is exactly the content you send in the request (no extra whitespace, newlines, or reformatting between hashing and sending).
- If you generate the body dynamically, compute the hash from the same in-memory bytes before sending.

## Example calls

Assume refunds.json contains the example array above.

1) curl (Windows PowerShell with curl alias or curl.exe):

$body = Get-Content -Raw -Path .\refunds.json
# Paste the hash you obtained from ssfd_ch4_ex3-warehouse
$hash = "<paste-hex-from-ssfd_ch4_ex3-warehouse>"

curl.exe -X POST "http://localhost:8080/api/refunds" ^
  -H "Content-Type: application/json" ^
  -H "X-Content-SHA3: $hash" ^
  --data "$body"

2) PowerShell Invoke-RestMethod:

$body = Get-Content -Raw -Path .\refunds.json
# Paste the hash you obtained from ssfd_ch4_ex3-warehouse
$hash = "<paste-hex-from-ssfd_ch4_ex3-warehouse>"

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/refunds" `
  -ContentType "application/json" `
  -Headers @{ "X-Content-SHA3" = $hash } `
  -Body $body

3) curl (Linux/macOS):

HASH="<paste-hex-from-ssfd_ch4_ex3-warehouse>"
curl -X POST "http://localhost:8080/api/refunds" \
  -H "Content-Type: application/json" \
  -H "X-Content-SHA3: $HASH" \
  --data-binary @refunds.json

Note: On Linux/macOS, prefer --data-binary @refunds.json to avoid newline/encoding changes.

## Expected responses

1) Success (hash matches and JSON is valid):
- Status: 200 OK
- Body: echoes the parsed refunds as JSON

Example:
[
  { "orderId": "10001", "amount": 120 },
  { "orderId": "10002", "amount": 450 }
]

2) Invalid or missing hash:
- Status: 400 Bad Request
- Body:
{
  "error": "invalid_hash",
  "message": "Invalid SHA3 hash for provided refunds file"
}

If the X-Content-SHA3 header is missing, the message will indicate it explicitly (e.g., "Missing X-Content-SHA3 header").

3) Invalid JSON format:
- Status: 400 Bad Request
- Body:
{
  "error": "bad_request",
  "message": "Invalid refunds JSON format"
}

## Implementation notes (for reference)

- Endpoint: POST /api/refunds
- Controller: RefundController
- Service: RefundService (verifies hash and parses JSON)
- Hashing: HashManager using MessageDigest SHA3-256 and HexFormat for hex encoding
- Model: Refund is a Java record with fields (String orderId, BigDecimal amount)
- Errors handled via GlobalExceptionHandler returning structured JSON
