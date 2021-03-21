import base64url from 'base64url';
export class Base64urlUtil {
  public static base64urlToArrayBuffer(string: string): ArrayBuffer {
    return base64url.toBuffer(string);
  }

  public static arrayBufferToBase64url(buffer: ArrayBuffer): string {
    return base64url.encode(Buffer.from(buffer));
  }
}
