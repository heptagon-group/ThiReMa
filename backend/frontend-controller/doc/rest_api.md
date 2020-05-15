# Formato header sicurezza
```Authorization```: ```Bearer $token ```

# Formati JSON body
## Autenticazione
Esempio richiesta:
```json
{
  "username": "Foo Bar",
  "password": "pass",
  "rememberMe": true
}
```

Esempio risposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiYXV0aG9yaXRpZXMiOlsidXNlciJdLCJpYXQiOjE1ODQ1NzM0ODEsImV4cCI6MTU4NDU3NzA4MX0.0yPd1tG-pz0DKSodQSN96O1gFWjTb9GXARsLkpx4Dj4"
}
```

## Utente
Note:
* campo ```vatNumber``` opzionale.

Esempio:
```json
{
  "id": 1,
  "username": "Foo Bar",
  "email": "foobar@mail.com",
  "telegramUsername": "foobar",
  "password": "pass",
  "vatNumber": "11111111111"
}
```

### Gateway
Esempio:
```json
{
  "id": 1
}
```

## Device
Note:
* campo ```frequency``` espresso in secondi.

Esempio:
```json
{
  "id": 1,
  "name": "Heating System",
  "ipAddress": "127.0.0.1",
  "brand": "Sun Tech",
  "model": "G714",
  "frequency": 60,
  "gatewayId": 1
}
```

## Configurazione
Esempio:
```json
{
  "id": 1,
  "name": "Temperature",
  "format": "°C",
  "threshold": 40,
  "influential": true
}
```

## Errore
Note:
* campo ```fieldErrors``` opzionale.

Esempio 1:
```json
{
  "timestamp": "18-03-2020 23:35:38",
  "status": "BAD_REQUEST",
  "message": "user not found"
}
```
Esempio 2:
```json
{
  "timestamp": "18-03-2020 23:35:38",
  "status": "CONFLICT",
  "message": "validation error",
  "fieldErrors": [
    {
      "field": "username",
      "message": "already exists"
    }
  ]
}
```

# Autenticazione
## POST ```/auth```
* Descrizione: verifica le credenziali di un utente e restituisce id e token.
* Richiesta: campi autenticazione.
* Risposta:
    * 200 (OK): restituisce il token;
    * 400 (Bad Request): campi authenticazione non validi  
    ```"must not be blank"```;
    * 401 (Unauthorized):
        * username o password errati  
        ```"bad credentials"```;
        * token scaduto  
        ```"expired token"```;
        * token non valido  
        ```"invalid token"```.

# Utente
## GET ```/user```
* Descrizione: restituisce tutti gli utenti.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): restituisce l'_array_ di utenti.

## POST ```/user```
* Descrizione: aggiunge un nuovo utente.
* Richiesta: tutti i campi utente tranne ```id```.
* Risposta:
    * 201 (Created): aggiunto nuovo utente;
    * 400 (Bad Request): campi utente non validi  
     ```"must not be blank"```;
    * 409 (Conflict): campi utente ```username```, ```email``` o ```telegramUsername``` già esistenti  
     ```"already exists"```.

## PATCH ```/user/{id}```
* Descrizione: modifica le informazioni di un utente.
* Richiesta: tutti i campi utente (opzionali), eccetto ```id```.
* Risposta:
    * 200 (OK): utente modificato;
    * 400 (Bad Request): campi utente non validi  
     ```"must be null or must not be blank"```;
    * 404 (Not Found): utente non trovato  
     ```"user not found"```;
    * 409 (Conflict): campi utente ```username```, ```email``` o ```telegramUsername``` già esistenti  
     ```"already exists"```.

## DELETE ```/user/{id}```
* Descrizione: elimina un utente.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): utente eliminato;
    * 404 (Not Found): utente non trovato  
     ```"user not found"```.

# Gateway
## GET ```/user/gateway```
* Descrizione: restituisce tutti i gateway dell'utente.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): restituisce l'_array_ di gateway.

## GET ```/user/gateway/new```
* Descrizione: aggiunge un nuovo gateway e ne restituisce l'id.
* Richiesta: _(vuota)_
* Risposta:
    * 201 (Created): restituisce il campo gateway ``id`` del nuovo gateway;

## DELETE ```/user/gateway/{id}```
* Descrizione: elimina un gateway e tutti i device associati.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): gateway e device associati eliminati;
    * 404 (Not Found): gateway non trovato  
    ```"gateway not found"```.

# Device
## GET ```/user/device```
* Descrizione: restituisce tutti i device dell'utente.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): restituisce l'_array_ di device.

## POST ```/user/device```
* Descrizione: aggiunge un nuovo device e ne restituisce l'id.
* Richiesta: tutti i campi device tranne ```id```.
* Risposta:
    * 201 (Created): restituisce il campo device ``id`` del nuovo device;
    * 400 (Bad Request): campi device non validi  
    ```"must not be blank"```;
    * 409 (Conflict): campo device ```ipAddress``` già esistente  
    ```"already exists"```.

## PATCH ```/user/device/{id}```
* Descrizione: modifica le informazioni di un device.
* Richiesta: tutti i campi device (opzionali), eccetto ```id```.
* Risposta:
    * 200 (OK): device modificato;
    * 400 (Bad Request): campi device non validi  
    ```"must be null or must not be blank"```;
    * 404 (Not Found): device non trovato  
    ```"device not found"```;
    * 409 (Conflict): campo device ```ipAddress``` già esistente  
    ```"already exists"```.

## DELETE ```/user/device/{id}```
* Descrizione: elimina un device.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): device eliminato;
    * 404 (Not Found): device non trovato  
    ```"device not found"```.

# Configurazioni
## GET ```/user/device/{deviceId}/config```
* Descrizione: restituisce tutte le configurazioni del device dell'utente.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): restituisce l'_array_ di configurazioni.

## POST ```/user/device/{deviceId}/config```
* Descrizione: aggiunge una nuova configurazione.
* Richiesta: tutti i campi configurazione tranne ```id```.
* Risposta:
    * 201 (Created): aggiunta nuova configurazione;
    * 400 (Bad Request): campi configurazione non validi  
    ```"must not be blank"```;
    * 409 (Conflict): campo configurazione ```name``` già esistente  
    ```"already exists"```.

## PATCH ```/user/device/{deviceId}/config/{configId}```
* Descrizione: modifica le informazioni di una configurazione.
* Richiesta: tutti i campi configurazione (opzionali), eccetto ```id```.
* Risposta:
    * 200 (OK): configurazione modificata;
    * 400 (Bad Request): campi configurazione non validi  
    ```"must be null or must not be blank"```;
    * 404 (Not Found): configurazione non trovata  
    ```"configuration not found"```;
    * 409 (Conflict): campo configurazione ```name``` già esistente  
    ```"already exists"```.

## DELETE ```/user/device/{deviceId}/config/{configId}```
* Descrizione: elimina una configurazione.
* Richiesta: _(vuota)_
* Risposta:
    * 200 (OK): configurazione eliminata;
    * 404 (Not Found): configurazione non trovata  
    ```"configuration not found"```.