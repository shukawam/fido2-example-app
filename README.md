# fido2-example-app

[Helidon](https://helidon.io/) と [WebAuthn4J](https://webauthn4j.github.io/webauthn4j/ja/) を使った FIDO2 - Relying Party のサンプルアプリケーションです．

## 全体構成

- front: WebAuthn を実行するクライアントのサンプル実装．
  - Angular
- rp-server: WebAuthn - Relying Party のサンプル実装．
  - Java
    - Helidon MP: for REST API
    - WebAuthn4J: Attestation, Assertion の検証ライブラリ

## 実行

```bash
# ./fido2-sample-app
docker compose up -d
```

[http://localhost](http://localhost) にブラウザからアクセスすると確認できます．
