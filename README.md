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
