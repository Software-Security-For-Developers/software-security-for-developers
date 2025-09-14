# SSFD Chapter 5 Exercise 4 — Payments Service

This Spring Boot service accepts an encrypted file containing a list of refunds, decrypts it, and returns the refunds as JSON.

Important: The encrypted file you upload is produced by the Warehouse service. This service only decrypts and reads that file; it does not generate it.


## Run the service

Prerequisites: Java 17+ and Maven.

- Using Maven
  - mvn spring-boot:run
- Using the packaged JAR
  - mvn clean package
  - java -jar target/ssfd_ch5_ex4-payments-0.0.1-SNAPSHOT.jar

By default, Spring Boot uses port 8080.


## Configure the AES key

The service decrypts the payload with AES/GCM/NoPadding. You can configure the key in one of two ways in `src/main/resources/application.properties`:

Provide a plaintext secret (recommended for simplicity in demos). A 256-bit key is derived via SHA-256 of the secret:
```
security.aes.secret=your-plaintext-secret
```

## Encryption payload format (produced by Warehouse)

The Warehouse service produces the encrypted payload in the following format (this service expects the same):
- 12 bytes: IV/nonce
- N bytes: Ciphertext concatenated with a 16‑byte GCM authentication tag

You may upload the payload either as raw bytes or as a Base64-encoded string of those bytes. The service will auto-detect Base64 and decode it if needed.


## Endpoint

- Method: POST
- Path: /api/refunds
- Request Content-Type: application/octet-stream
- Request Body: The encrypted payload (raw bytes or Base64-encoded text) produced by the Warehouse service.
- Response Content-Type: application/json
- Response Body: JSON array of refund objects

Refund object shape:
```
{
  "orderId": "<string>",
  "amount": <number>
}
```

Example decrypted JSON (what the Warehouse encrypted):
```
[
  { "orderId": "A-1001", "amount": 12.50 },
  { "orderId": "A-1002", "amount": 7.00 }
]
```


## Example: calling the endpoint

Assume the Warehouse service produced a file `refunds.enc` that contains the encrypted payload in the expected format.

Using curl

```
curl -X POST \
  http://localhost:8080/api/refunds \
  -H "Content-Type: application/octet-stream" \
  --data-binary @refunds.json.aesgcm
```

Example successful response:
```
[
  { "orderId": "A-1001", "amount": 12.5 },
  { "orderId": "A-1002", "amount": 7.0 }
]
```


## Errors you may see

- 400 Bad Request: "Encrypted payload too short for AES-GCM format"
  - The uploaded payload doesn’t contain at least a 12-byte IV and 16-byte tag.
- 400 Bad Request: "Failed to decrypt AES-GCM payload"
  - Wrong key, wrong IV/tag, or corrupted/incorrect payload.
- 400 Bad Request: "Invalid refunds JSON format"
  - Decryption succeeded but the plaintext is not a JSON array of refunds.
- 500 Internal Server Error: "AES key is not configured" or key format issues
  - Ensure `security.aes.key` is set and is valid Base64 for a 16/24/32 byte key.

