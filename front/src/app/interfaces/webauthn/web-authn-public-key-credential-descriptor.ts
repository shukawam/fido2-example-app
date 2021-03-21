export interface WebAuthnPublicKeyCredentialDescriptor {
  type: PublicKeyCredentialType;
  id: string;
  transports?: AuthenticatorTransport[];
}
