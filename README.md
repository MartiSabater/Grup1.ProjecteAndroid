# PROJECTE ANDROID GRUP 1 QP 26

## Android - Asistente IA

Per l'exercici 1, en el que s'havia d'implementar un assistent IA/LLM, he afegit una nova pantalla dins de l'aplicacio Android per poder fer preguntes i veure la resposta de l'assistent.

La idea principal es que l'usuari no hagi de sortir de l'app per consultar dubtes. Desde Android s'escriu una pregunta, es prem el boto `PREGUNTAR`, i l'app fa una peticio amb Retrofit al nostre backend. Despres el backend s'encarrega de parlar amb el LLM de la UPC i retorna la resposta a Android.

## Que he afegit

He creat una nova Activity:

- AssistentActivity

Tambee he creat el layout de la pantalla:

app/src/main/res/layout/activity_assistant.xml

Aquesta pantalla te:

- Un titol de `ASISTENTE IA`.
- Un camp per escriure la pregunta.
- Un boto `PREGUNTAR`.
- Una zona on es mostra la resposta.
- Un boto `VOLVER` per tornar enrere.

## Connexio amb el backend

Per enviar la pregunta al backend he afegit els models:

AssistentRequest.java
AssistentResponse.java

I he afegit el endpoint a Retrofit:

POST /assistant/ask


El format que envia Android es:

  "question": "Que articles puc comprar a la botiga?"

I el format que espera rebre es:

  "answer": "Resposta generada per l'assistent"


## Integracio dins de l'app

He afegit un boto `ASISTENTE IA` a la pantalla de la botiga:


app/src/main/res/layout/activity_shop.xml


I desde `ShopActivity.java` aquest boto obre la nova pantalla de l'assistent:


app/src/main/java/com/example/sigmadsa/viewmodel/ShopActivity.java


Tambee he registrat la nova Activity al manifest:

app/src/main/AndroidManifest.xml


## Com funciona

Android no crida directament al LLM. Android nomes crida al nostre backend amb Retrofit. Aixo es important perque el minim demana que les funcionalitats passin per una API REST.

## Primeres proves

Primer he provat que la pantalla s'obria correctament desde la botiga.

Despres he provat preguntes relacionades amb l'app, per exemple:

Que articles puc comprar a la botiga?


I tambe preguntes externes per comprovar que no era nomes una resposta dummy, per exemple:

Explica que es Docker en una frase


Com que Docker no forma part de les preguntes dummy de la botiga, si Android mostra una resposta sobre Docker vol dir que la resposta esta venint del LLM.

## Que funciona

- La pantalla de l'assistent s'obre desde la botiga.
- L'usuari pot escriure una pregunta.
- El boto `PREGUNTAR` envia la pregunta amb Retrofit.
- Android rep la resposta del backend i la mostra en pantalla.
- S'ha comprovat amb preguntes sobre la botiga.
- S'ha comprovat amb preguntes externes com Docker per verificar que passa pel LLM.
- Si el backend utilitza el fallback dummy, Android tambe pot mostrar aquesta resposta.

## Proves en local

Per provar en local amb l'emulador, la base URL d'Android ha de ser:

http://10.0.2.2:8080/dsaApp/


Aquesta URL apunta al backend local desde l'emulador.

Si es prova amb un mobil fisic, s'ha d'utilitzar la IP de l'ordinador on esta corrent el backend.

## Fallos coneguts

Les proves fetes estan en local per el moment, per no crear problemes amb el git ni amb la configuracio dels meus companys.

# SigmaDSA - EJ2

## Estat de l'exercici

L'EJ2 esta acabat.

S'ha implementat la funcionalitat que permet consultar els membres de l'equip de l'usuari identificat. La funcionalitat esta integrada entre l'aplicacio Android i l'API REST utilitzant Retrofit.

## Repositori GitHub

Link del repositori:

[https://github.com/Martatm18/Grup-1.-Projecte-DSA-QP-26/tree/minim2_Marti](https://github.com/Martatm18/Grup-1.-Projecte-DSA-QP-26/tree/minim2_Marti)

## Que s'ha implementat

### Android

S'ha afegit una nova pantalla `TeamActivity` que mostra la informacio de l'equip de l'usuari.

La pantalla es pot obrir des de `ShopActivity` amb el boto `Mi equipo`.

La pantalla mostra:

- Nom de l'equip.
- Llistat de membres de l'equip.
- Nom de cada membre.
- Punts de cada membre.
- Avatar local de cada membre.

Fitxers principals modificats o afegits:

- `TeamActivity.java`
- `activity_team.xml`
- `team_member_item.xml`
- `TeamResponse.java`
- `TeamMemberResponse.java`
- `ApiService.java`
- `AndroidManifest.xml`
- `activity_shop.xml`
- `ShopActivity.java`

Tambe s'han afegit avatars locals a:

```text
SigmaDSA/app/src/main/res/drawable-nodpi/
```

Els avatars es carreguen des del codi Android com a recursos locals:

```text
R.drawable.avatar_1
R.drawable.avatar_2
...
R.drawable.avatar_12
```

### API REST

S'ha afegit la ruta REST necessaria per consultar l'equip de l'usuari:

```text
GET /user/{idUser}/team
```

La funcionalitat Android fa la peticio a aquesta ruta amb Retrofit.

## Resposta de l'API REST

La resposta de l'API REST es:

```json
{
  "members": [
    {
      "avatar": "avatar_3",
      "name": "Marta",
      "points": 250
    },
    {
      "avatar": "avatar_4",
      "name": "Carla",
      "points": 200
    },
    {
      "avatar": "avatar_2",
      "name": "Marti",
      "points": 380
    },
    {
      "avatar": "avatar_5",
      "name": "Hector",
      "points": 180
    }
  ],
  "team": "Grup1"
}
```

## Funcionament

Flux de la funcionalitat:

1. L'usuari inicia sessio a l'aplicacio.
2. L'usuari entra a la pantalla de botiga.
3. L'usuari prem el boto `Mi equipo`.
4. Android obre `TeamActivity`.
5. `TeamActivity` fa una peticio Retrofit a `GET /user/{idUser}/team`.
6. L'API REST retorna el nom de l'equip i els membres.
7. Android mostra el nom de l'equip, els membres, els punts i els avatars.

## Enunciat de l'exercici EJ2

Nova funcionalitat que permeti consultar els membres de l'equip del qual forma part l'usuari.

Tasques demanades:

- En l'aplicacio Android, afegir una nova activitat que proporcioni un llistat dels usuaris que comparteixen equip amb l'usuari identificat.
- Per cada usuari del llistat s'ha de mostrar imatge, nom i punts.
- Afegir una nova ruta al backend que rebi la consulta:

```text
GET /user/{idUser}/team
```

- La funcionalitat ha de fer una peticio a l'API utilitzant Retrofit.

## Estat final

La funcionalitat EJ2 esta implementada i integrada amb l'API REST.
