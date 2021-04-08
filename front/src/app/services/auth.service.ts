import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private readonly router: Router) {}

  isAuthenticated(): boolean {
    const code = decodeURIComponent(
      this.router.url.split('?')[1].split('=')[1]
    );
    return code !== '' ? true : false;
  }
}
