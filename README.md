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

Si es prova fora de local, s'ha de revisar:

- La base URL de `ApiClient.java`.
- La `BASE_URI` del backend.
- Que el backend estigui pujat a produccio
- Que el dispositiu Android pugui arribar a la IP del backend.