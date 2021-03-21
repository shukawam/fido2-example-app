import { Injectable } from '@angular/core';
import { AssertionResult } from '../interfaces/webauthn/assertion-result';
import { WebauthnService } from './webauthn.service';

@Injectable({
  providedIn: 'root',
})
export class SignInService {
  constructor(private readonly webAuthnService: WebauthnService) {}

  public async requestCredential(email: string): Promise<Credential | null> {
    return this.webAuthnService.requestCredential(email, {
      // If you want to determine the specification on the client side, write the options here.
      userVerification: 'preferred',
    });
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
