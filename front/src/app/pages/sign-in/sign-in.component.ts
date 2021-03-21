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
    let publicKeyCredential: PublicKeyCredential = credential as PublicKeyCredential;
    let assertionResponse: AuthenticatorAssertionResponse = publicKeyCredential.response as AuthenticatorAssertionResponse;
    let credentialId = publicKeyCredential.rawId;
    let clientDataJSON = assertionResponse.clientDataJSON;
    let authenticatorData = assertionResponse.authenticatorData;
    let signature = assertionResponse.signature;
    let userHandle = assertionResponse.userHandle;
    let clientExtensions = publicKeyCredential.getClientExtensionResults();
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
