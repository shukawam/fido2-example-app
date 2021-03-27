import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Profile } from 'src/app/interfaces/webauthn/profile';
import { SignInService } from 'src/app/services/sign-in.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css'],
})
export class SignInComponent implements OnInit {
  user: Profile = {
    email: '',
  };
  message = '';
  isSuccess = false;

  constructor(
    private readonly signInService: SignInService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {}

  async signIn(): Promise<void> {
    const credential = await this.signInService.requestCredential(
      this.user.email
    );
    const publicKeyCredential: PublicKeyCredential = credential as PublicKeyCredential;
    const assertionResponse: AuthenticatorAssertionResponse = publicKeyCredential.response as AuthenticatorAssertionResponse;
    const credentialId = publicKeyCredential.rawId;
    const clientDataJSON = assertionResponse.clientDataJSON;
    const authenticatorData = assertionResponse.authenticatorData;
    const signature = assertionResponse.signature;
    const userHandle = assertionResponse.userHandle;
    const clientExtensions = publicKeyCredential.getClientExtensionResults();
    const result = await this.signInService.checkCredential(
      credentialId,
      clientDataJSON,
      authenticatorData,
      signature,
      userHandle
    );
    if (result.isSuccess === true) {
      this.router.navigate(['/login-success']);
    } else {
      this.message = 'ログイン失敗！';
    }
  }
}
