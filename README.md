# fido2-example

[Helidon](https://helidon.io/)と[WebAuthn4J](https://webauthn4j.github.io/webauthn4j/ja/)を使った FIDO2 - Relying Party のサンプル実装．

## 構成

- front: WebAuthn を実行するクライアントのサンプル実装．
  - Angular
- server: Relying Party のサンプル実装．
  - Java
    - Helidon MP: for REST API
    - WebAuthn4J: Attestation, Assertion の検証ライブラリ

## 実行

```bash
cd <clone dir>
docker-compose up -d
```

[http://localhost](http://localhost)
