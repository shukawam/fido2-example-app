export interface WebAuthn4NgCredentialRequestOptions {
  challenge?: BufferSource;
  timeout?: number;
  rpId?: string;
  allowCredentials?: PublicKeyCredentialDescriptor[];
  userVerification?: 'required' | 'preferred' | 'discouraged';
  extensions?: AuthenticationExtensionsClientInputs;
}
