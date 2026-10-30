# Manual test cases

The API test cases this framework automates, written as a tester would run them by hand (curl,
Postman or a browser). Every case has an ID; the TestNG method that covers it starts with the same ID
(`tc201_validateEmailFormatInCommentsOfSearchedUser`) and so does its description (`[TC-201] ...`),
so a failure in the report leads straight to the case below.

## Setup

- Base URL: <https://jsonplaceholder.typicode.com> (a public fake API, `application-data.properties`).
- Test user: username `Samantha`.
- Valid e-mail: `^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$`, e.g. `name@example.com`.

## Summary

| ID | Title | Priority | Automated by | Suite |
|---|---|---|---|---|
| TC-201 | Every e-mail in the comments on the searched user's posts is valid | High | `Task.tc201_validateEmailFormatInCommentsOfSearchedUser` | `smoke-testng.xml` (CI) |
| TC-202 | All users are returned, each with an id | Medium | `APITest.tc202_fetchUsers` | not in a suite: run from the IDE |

---

### TC-201 Every e-mail in the comments on the searched user's posts is valid

| # | Step | Expected result |
|---|---|---|
| 1 | `GET /users?username=Samantha` | `200`; one user; note the `id` (e.g. `3`) |
| 2 | `GET /posts?userId=<id>` | `200`; the user's posts |
| 3 | For each post `GET /posts/<post id>/comments` (or `/comments?postId=<post id>`) | `200` each; the comments |
| 4 | Check every comment's `email` against the pattern above | All valid. Note **every** invalid address with its comment id before failing the case |

### TC-202 All users are returned, each with an id

| # | Step | Expected result |
|---|---|---|
| 1 | `GET /users` | `200` |
| 2 | Check each user | Every user has a non-empty `id` |
