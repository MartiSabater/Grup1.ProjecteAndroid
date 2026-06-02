# Implementation Plan - TeamActivity Logic

This plan describes the implementation of the logic for `TeamActivity.java`, including fetching team data from the API and dynamically inflating the team member list.

## Proposed Changes

### [TeamActivity.java](file:///C:/Users/marti/dsa/SigmaDSA/Grup1.ProjecteAndroid/SigmaDSA/app/src/main/java/com/example/sigmadsa/viewmodel/TeamActivity.java)
- **`onCreate`**:
    - Set content view to `activity_team`.
    - Hide Action Bar.
    - Retrieve `userId` from Intent extras using `LoadingActivity.EXTRA_USER_ID`.
    - Initialize `ApiService`.
    - Find views: `tv_team_name`, `ll_members_container`.
    - Call `cargarEquipo()`.
- **`cargarEquipo()`**:
    - Make the API call `apiService.getUserTeam(userId)`.
    - Handle success by updating the team name and calling `mostrarMiembros()`.
    - Handle failure by showing a `Toast`.
- **`mostrarMiembros(List<TeamMemberResponse> members)`**:
    - Clear the `ll_members_container`.
    - Iterate through the member list.
    - Inflate `team_member_item.xml` for each member.
    - Set the name and points in the inflated view.
    - Add the view to the container.

## Verification Plan

### Static Analysis
- Use `analyze_file` to check for syntax errors or missing imports.
- Ensure all view IDs match `activity_team.xml` and `team_member_item.xml`.
