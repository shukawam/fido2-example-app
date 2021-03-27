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
    const credential = await this.webAuthnService.createCredential(email);
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
