import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AssertionResult } from '../interfaces/webauthn/assertion-result';
import { AssertionServerOptions } from '../interfaces/webauthn/assertion-server-options';
import { AttestationResult } from '../interfaces/webauthn/attestation-result';
import { AttestationServerOptions } from '../interfaces/webauthn/attestation-server-options';
import { WebAuthn4NgCredentialCreationOptions } from '../interfaces/webauthn/web-authn-4-ng-credential-creation-options';
import { WebAuthn4NgCredentialRequestOptions } from '../interfaces/webauthn/web-authn-4-ng-credential-request-options';
import { Base64urlUtil } from '../utils/base64url';

@Injectable({
  providedIn: 'root',
})
export class WebauthnService {
  private ATTESTATION_OPTION = '/webauthn/attestation/options';
  private ATTESTATION_RESULT = '/webauthn/attestation/result';
  private ASSERTION_OPTION = '/webauthn/assertion/options';
  private ASSERTION_REQUEST = '/webauthn/assertion/result';

  constructor(private readonly httpClient: HttpClient) {}

  public async createCredential(
    email: string,
    publicKeyCredentialCreationOptions: WebAuthn4NgCredentialCreationOptions
  ): Promise<Credential | null> {
    return this.fetchAttestationOptions(email).then((fetchOptions) => {
      let mergeOptions = {
        ...fetchOptions,
        ...publicKeyCredentialCreationOptions,
      };
      let credentialCreationOptions: CredentialCreationOptions = {
        publicKey: mergeOptions,
      };
      console.log('credentialCreationOption', credentialCreationOptions);
      return navigator.credentials.create(credentialCreationOptions);
    });
  }

  private fetchAttestationOptions(
    email: string
  ): Promise<PublicKeyCredentialCreationOptions> {
    return this.httpClient
      .get<AttestationServerOptions>(`${this.ATTESTATION_OPTION}/${email}`, {
        headers: {
          'Content-type': 'application/json',
        },
      })
      .toPromise()
      .then((serverOptions) => {
        console.log('serverOptions', serverOptions);
        return {
          // require
          rp: serverOptions.rp,
          user: {
            id: Base64urlUtil.base64urlToArrayBuffer(serverOptions.user.id),
            name: serverOptions.user.name,
            displayName: serverOptions.user.displayName,
          },
          challenge: Base64urlUtil.base64urlToArrayBuffer(
            serverOptions.challenge
          ),
          pubKeyCredParams: [
            {
              alg: -7,
              type: 'public-key',
            },
          ],
          // option
          timeout: serverOptions.timeout,
          excludeCredentials: serverOptions.excludeCredentials.map(
            (credential) => {
              return {
                type: credential.type,
                id: Base64urlUtil.base64urlToArrayBuffer(credential.id),
                transports: credential.transports,
              };
            }
          ),
          authenticatorSelection: serverOptions.authenticatorSelection,
          attestation: serverOptions.attestation,
          extensions: serverOptions.extensions,
        };
      });
  }

  public async registerAttestationObject(
    email: string,
    authenticatorAttestationResponse: AuthenticatorAttestationResponse
  ): Promise<AttestationResult> {
    return this.httpClient
      .post<AttestationResult>(
        `${this.ATTESTATION_RESULT}`,
        {
          email: email,
          attestationObject: Base64urlUtil.arrayBufferToBase64url(
            authenticatorAttestationResponse.attestationObject
          ),
          clientDataJSON: Base64urlUtil.arrayBufferToBase64url(
            authenticatorAttestationResponse.clientDataJSON
          ),
        },
        {
          observe: 'body',
        }
      )
      .toPromise();
  }

  public async requestCredential(
    email: string,
    publicKeyCredentialRequestOptions: WebAuthn4NgCredentialRequestOptions
  ): Promise<Credential | null> {
    return this.fetchAssertionOptions(email).then((fetchedOptions) => {
      let mergedOptions = {
        ...fetchedOptions,
        ...publicKeyCredentialRequestOptions,
      };
      let credentialRequestOptions: CredentialRequestOptions = {
        publicKey: mergedOptions,
      };
      console.log('credentialRequestOptions', credentialRequestOptions);
      return navigator.credentials.get(credentialRequestOptions);
    });
  }

  public checkCredential(
    credentialId: ArrayBuffer,
    clientDataJSON: ArrayBuffer,
    authenticatorData: ArrayBuffer,
    signature: ArrayBuffer,
    userHandle: ArrayBuffer
  ): Promise<AssertionResult> {
    return this.httpClient
      .post<AssertionResult>(
        `${this.ASSERTION_REQUEST}`,
        {
          credentialId: Base64urlUtil.arrayBufferToBase64url(credentialId),
          clientDataJSON: Base64urlUtil.arrayBufferToBase64url(clientDataJSON),
          authenticatorData: Base64urlUtil.arrayBufferToBase64url(
            authenticatorData
          ),
          signature: Base64urlUtil.arrayBufferToBase64url(signature),
          userHandle: Base64urlUtil.arrayBufferToBase64url(userHandle),
        },
        {
          observe: 'body',
        }
      )
      .toPromise();
  }

  private fetchAssertionOptions(
    email: string
  ): Promise<PublicKeyCredentialRequestOptions> {
    console.log(email);
    return this.httpClient
      .get<AssertionServerOptions>(`${this.ASSERTION_OPTION}/${email}`)
      .toPromise()
      .then((requestOptions) => {
        return {
          // require
          challenge: Base64urlUtil.base64urlToArrayBuffer(
            requestOptions.challenge
          ),
          // option
          timeout: requestOptions.timeout,
          rpId: requestOptions.rpId,
          allowCredentials: requestOptions.allowCredentials?.map(
            (allowCredential) => {
              return {
                type: 'public-key',
                id: Base64urlUtil.base64urlToArrayBuffer(allowCredential.id),
                transports: allowCredential.transports,
              };
            }
          ),
          userVerification: requestOptions.userVerification,
          extensions: requestOptions.extensions,
        };
      });
  }
}
