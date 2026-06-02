# SigmaDSA - EJ2

## Abast de la implementacio

S'ha implementat la funcionalitat de consulta dels membres de l'equip de l'usuari identificat.

La funcionalitat permet que, des de l'aplicacio Android, l'usuari accedeixi a una nova pantalla on es mostra:

- Nom de l'equip.
- Llistat de membres de l'equip.
- Nom de cada membre.
- Punts de cada membre.
- Avatar local associat a cada membre.

## Part Android

S'ha afegit una nova pantalla `TeamActivity` encarregada de consultar i mostrar la informacio de l'equip.

La pantalla es pot obrir des de `ShopActivity` mitjancant el boto `Mi equipo`. En obrir-se, rep el `userId` de l'usuari actual i fa una peticio a l'API amb Retrofit.

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

Els avatars es referencien des del codi Android com a recursos locals, per exemple:

```text
R.drawable.avatar_1
R.drawable.avatar_2
...
R.drawable.avatar_12
```

## Part API

S'ha afegit la ruta REST necessaria per consultar l'equip de l'usuari:

```text
GET /user/{idUser}/team
```

La resposta esperada conte el nom de l'equip i una llista de membres:

```json
{
  "team": "porxinos",
  "members": [
    {
      "name": "Juan",
      "avatar": "avatar_1",
      "points": 250
    },
    {
      "name": "Palomo",
      "avatar": "avatar_2",
      "points": 200
    }
  ]
}
```

## Funcionament

Flux de la funcionalitat:

1. L'usuari inicia sessio.
2. L'usuari entra a la pantalla de botiga.
3. L'usuari prem el boto `Mi equipo`.
4. Android obre `TeamActivity`.
5. `TeamActivity` fa una peticio Retrofit a `GET /user/{idUser}/team`.
6. L'API retorna l'equip i els membres.
7. Android mostra el nom de l'equip, els membres, els punts i els avatars.

## Estat

La funcionalitat EJ2 esta implementada a Android i integrada amb l'API REST.
