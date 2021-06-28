# fido2-example-app

Sample application of FIDO2 that using [Helidon](https://helidon.io/#/)(for REST API) and [WebAuthn4J](https://github.com/webauthn4j)(for Attestation and Assertion validation).

## Overall composition

- front: Sample implementation of WebAuthn client.
  - Angular
- server: Sample implementation of FIDO2-RP
  - Java
    - Helidon MP: for REST API
    - WebAuthn4J: for Attestation and Assertion validation library

## Build and Run

```bash
# ./fido2-example-app
docker compose up -d
```
and access [http://localhost](http://localhost).
