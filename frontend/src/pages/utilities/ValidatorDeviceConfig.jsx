// to do
export default class ValidatorDeviceConfig {
  configNameLengthCheck(name) {
    if (name.length < 3) {
      return "Nome inserito troppo corto";
    }
    return null;
  }

  configNameTooLong(name) {
    if (name.length > 32) {
      return "Nome inserito troppo lungo";
    }
    return null;
  }

  checkIfIsNotNumber(format) {
    return isNaN(format);
  }
}
