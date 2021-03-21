import { Injectable } from '@angular/core';
import { WebauthnService } from './webauthn.service';
import { AttestationResult } from '../interfaces/webauthn/attestation-result';

@Injectable({
  providedIn: 'root',
})
export class SignUpService {
  constructor(private readonly webAuthnService: WebauthnService) {}

  public async createCredential(
    userHandleBase64: string,
    username: string,
    email: string,
    displayName: string
  ): Promise<Credential | null> {
    // let excludeCredentials: PublicKeyCredentialDescriptor[] = credentialIds.map(
    //   (credentialId) => {
    //     // noinspection UnnecessaryLocalVariableJS
    //     let credential: PublicKeyCredentialDescriptor = {
    //       type: 'public-key',
    //       id: credentialId,
    //     };
    //     return credential;
    //   }
    // );
    const credential = await this.webAuthnService.createCredential(email, {
      attestation: 'direct',
    });
    console.log('credential', credential);
    return credential;
  }

  public async registCredential(
    email: string,
    authenticatorAttestationResponse: AuthenticatorAttestationResponse
  ): Promise<AttestationResult> {
    return this.webAuthnService.registerAttestationObject(
      email,
      authenticatorAttestationResponse
    );
  }
}
