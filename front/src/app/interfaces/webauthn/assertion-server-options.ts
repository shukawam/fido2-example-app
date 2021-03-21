import { WebAuthnPublicKeyCredentialDescriptor } from './web-authn-public-key-credential-descriptor';

export interface AssertionServerOptions {
  challenge: string;
  timeout?: number;
  rpId?: string;
  allowCredentials?: WebAuthnPublicKeyCredentialDescriptor[];
  userVerification?: UserVerificationRequirement;
  extensions?: AuthenticationExtensionsClientInputs;
}
