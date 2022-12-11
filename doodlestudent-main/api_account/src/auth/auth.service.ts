import { Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { Account } from 'src/account/account.entity';
import { AccountService } from 'src/account/account.service';
import * as bcrypt from 'bcrypt';

@Injectable()
export class AuthService {
    constructor(private service: AccountService, private jwtService: JwtService) {}

    public async validateUser(mail: string, password: string): Promise<Account> {
        const account: Promise<Account> = this.service.getAccount(mail);
        if (
          account != undefined &&
          (await bcrypt.compare(password, (await account).password))
        ) {
          return account;
        }
        return undefined;
      }

      async login(user: any) {
        const payload = { username: user.id };
        return {
            access_token: this.jwtService.sign(payload),
        };
    }
}
