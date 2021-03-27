import { Injectable } from '@angular/core';
import { AssertionResult } from '../interfaces/webauthn/assertion-result';
import { WebauthnService } from './webauthn.service';

@Injectable({
  providedIn: 'root',
})
export class SignInService {
  constructor(private readonly webAuthnService: WebauthnService) {}

  public async requestCredential(email: string): Promise<Credential | null> {
    const credential = await this.webAuthnService.requestCredential(email);
    console.log('Credential', credential);
    return credential;
  }

  public async checkCredential(
    credentialId: ArrayBuffer,
    clientDataJSON: ArrayBuffer,
    authenticatorData: ArrayBuffer,
    signature: ArrayBuffer,
    userHandle: ArrayBuffer | null
  ): Promise<AssertionResult> {
    let handle;
    if (userHandle === null) {
      handle = new ArrayBuffer(0);
    } else {
      handle = userHandle;
    }
    return await this.webAuthnService.checkCredential(
      credentialId,
      clientDataJSON,
      authenticatorData,
      signature,
      handle
    );
  }
}
