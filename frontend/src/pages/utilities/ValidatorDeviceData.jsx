export default class ValidatorDeviceData {
  deviceNameLengthCheck(name) {
    if (name.length < 3) {
      return "Nome del device troppo corto";
    } else {
      return "";
    }
  }

  deviceNameTooLong(name) {
    if (name.length > 32) {
      return "Nome del device troppo lungo";
    }
    return "";
  }

  /**
   * accetta formati del tipo 192.168.1.1
   */
  ipAndUuidFormatCheck(data) {
    if (
      /^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.?){4}$/.test(data) ||
      /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/.test(
        data
      )
    ) {
      return "";
    }
    return "Formato IP/UUID non valido";
  }

  deviceBrandCheck(brand) {
    if (brand.length < 2) {
      return "Marca del device troppo corta";
    }
    return "";
  }

  brandTooLong(brand) {
    if (brand.length > 32) {
      return "Nome del brand troppo lungo";
    }
    return "";
  }

  deviceModelCheck(model) {
    if (model.length < 2) {
      return "Modello del device troppo corto";
    }
    return "";
  }

  modelTooLong(model) {
    if (model.length > 32) {
      return "Modello del device troppo lungo";
    }
    return "";
  }

  frequencyCheck(frequency) {
    if (isNaN(frequency)) {
      return "Inserire la frequenza di ricezione dei dati solamente in numeri";
    }
    return "";
  }

  gatewayFormatCheck(gateway) {
    if (
      /^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$/.test(
        gateway
      ) ||
      /^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$/.test(
        gateway
      ) ||
      /^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\..^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$/.test(
        gateway
      )
    ) {
      return "";
    } else {
      return "Formato del gateway scorretto";
    }
  }
}
