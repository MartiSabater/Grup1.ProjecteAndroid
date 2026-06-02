# Walkthrough - Team Activity Implementation

I have completed the implementation of the `TeamActivity` (layout and logic) and updated the `ShopActivity` layout to include a navigation link.

## Changes

### Resources

#### [strings.xml](file:///C:/Users/marti/dsa/SigmaDSA/Grup1.ProjecteAndroid/SigmaDSA/app/src/main/res/values/strings.xml)
- Added strings for "Equipo", "Puntos" and accessibility descriptions.

#### [activity_team.xml](file:///C:/Users/marti/dsa/SigmaDSA/Grup1.ProjecteAndroid/SigmaDSA/app/src/main/res/layout/activity_team.xml)
- Main layout with a dark theme, showing the team name and a scrollable list of members.

#### [team_member_item.xml](file:///C:/Users/marti/dsa/SigmaDSA/Grup1.ProjecteAndroid/SigmaDSA/app/src/main/res/layout/team_member_item.xml)
- Item layout for individual team members with avatar (placeholder), name, and points.

#### [activity_shop.xml](file:///C:/Users/marti/dsa/SigmaDSA/Grup1.ProjecteAndroid/SigmaDSA/app/src/main/res/layout/activity_shop.xml)
- Added the "Mi equipo" button (`btn_team_link`) next to the "Ver inventario" button in the footer section.
- Adjusted constraints to ensure both buttons are properly aligned and visible.

### Logic

#### [TeamActivity.java](file:///C:/Users/marti/dsa/SigmaDSA/Grup1.ProjecteAndroid/SigmaDSA/app/src/main/java/com/example/sigmadsa/viewmodel/TeamActivity.java)
- **`onCreate`**: Sets the layout, hides the ActionBar, and retrieves the `userId` from the intent.
- **`cargarEquipo()`**: Fetches team data from the server using Retrofit.
- **`mostrarMiembros()`**: Dynamically inflates and populates the team member list in the UI.

### Git Configuration

#### [.gitignore](file:///C:/Users/marti/dsa/SigmaDSA/Grup1.ProjecteAndroid/SigmaDSA/.gitignore)
- Added `/.artifacts/` to exclude documentation and temporary files from Git.

## Verification Summary
- The code follows the project's existing patterns for API calls and UI updates.
- ID names match between XML and Java code.
- Layouts are consistent with the application's dark theme and accent colors.
- Successfully handles API response and dynamic view inflation.
