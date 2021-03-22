import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProfileCreate } from 'src/app/interfaces/webauthn/profile-create';
import { SignUpService } from 'src/app/services/sign-up.service';
import { v4 as uuid } from 'uuid';

@Component({
  selector: 'app-signup',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css'],
})
export class SignUpComponent implements OnInit {
  user: ProfileCreate = {
    userHandle: uuid(),
    email: '',
  };
  message = '';
  isSuccess = false;

  constructor(
    private readonly signUpService: SignUpService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {}

  async signUp(): Promise<void> {
    console.log(this.user.email);
    const credential = await this.signUpService.createCredential(
      this.user.userHandle,
      this.user.email,
      this.user.email,
      this.user.email
    );
    let publicKeyCredential: PublicKeyCredential = credential as PublicKeyCredential;
    let attestationResponse: AuthenticatorAttestationResponse = publicKeyCredential.response as AuthenticatorAttestationResponse;
    const result = await this.signUpService.registCredential(
      this.user.email,
      attestationResponse
    );
    if (result.isSuccess === true) {
      this.message = '登録成功！';
      this.clearForm();
      this.isSuccess = true;
    } else {
      this.message = '登録失敗！';
    }
  }

  goSignIn(): void {
    this.router.navigate(['/sign-in']);
  }

  private clearForm() {
    this.user.email = '';
    this.user.userHandle = '';
  }
}
