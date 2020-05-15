export default class Validator {
  pswMatch(psw1, psw2) {
    if (!psw1 || psw1 !== psw2) {
      return "Le due password non coincidono";
    }
    return null;
  }

  isUsernameLengthValid(username) {
    if (username.length < 3) {
      return "L'username inserito è troppo corto";
    }
    return null;
  }

  isUsernameTooLong(username) {
    if (username.length > 32) {
      return "L'username inserito è troppo lungo";
    }
    return null;
  }

  isTelegramUsernameLengthValid(telegramUsername) {
    if (telegramUsername.length < 3) {
      return "L'username telegram inserito è troppo corto";
    }
    return null;
  }
  isTelegramUsernameTooLong(telegramUsername) {
    if (telegramUsername.length > 32) {
      return "L'username telegram inserito è troppo lungo";
    }
    return null;
  }

  isPswLongEnough(psw) {
    if (psw.length < 3) {
      return "La password inserita non è abbastanza lunga";
    }
    return null;
  }

  isPswTooLong(psw) {
    if (psw.length > 32) {
      return "La password inserita è troppo lunga";
    }
    return null;
  }

  isVatNumberLongEnough(vatNumber) {
    if (vatNumber.length !== 11) {
      return "La partita IVA inserita non è abbastanza lunga";
    }
    return null;
  }

  isEmailValid(email) {
    const lowerCaseEmail = email.toLowerCase();
    if (
      /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(lowerCaseEmail) ===
      false
    ) {
      return "Formato email non valido";
    }
    return null;
  }

  isEmailTooLong(email) {
    if (email.length > 254) {
      return "L'email inserita è troppo lunga";
    }
    return null;
  }

  isLoginOkay(username, psw) {
    if (username.length < 3 || psw.length <= 7) {
      return "Username o password scorretti";
    }
    return null;
  }
}
