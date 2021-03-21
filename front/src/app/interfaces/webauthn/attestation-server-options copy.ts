import { WebAuthnPublicKeyCredentialUserEntity } from './web-authn-public-key-credential-user-entity';
import { WebAuthnPublicKeyCredentialDescriptor } from './web-authn-public-key-credential-descriptor';

export interface AttestationServerOptions {
  rp: PublicKeyCredentialRpEntity;
  user?: WebAuthnPublicKeyCredentialUserEntity;
  challenge: string;
  pubKeyCredParams: PublicKeyCredentialParameters[];
  timeout?: number;
  excludeCredentials?: WebAuthnPublicKeyCredentialDescriptor[];
  authenticatorSelection?: AuthenticatorSelectionCriteria;
  attestation?: AttestationConveyancePreference;
  extensions?: AuthenticationExtensionsClientInputs;
}
