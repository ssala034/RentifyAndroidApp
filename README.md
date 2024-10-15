# Rentify

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/_bwx64jP)

## Application Flows
### Login

#### Normal Flow
User inputs a username or email already existing in the system and the associated password.
The system retrieves the account from the database and sets it to the system state.

#### Validation
1. The system validates whether the two fields are not empty.
1. The system validates whether the login credentials are authentic.
For both the case where the email or username does not exist or the case where the password is incorrect,
the system will display an "incorrect username or password" message.

### Register

#### Normal Flow
User inputs a username, email, and password and selects a role.
The user can optionally input a first name and last name.
The system creates an account, saves it to the database, and sets it to the system state.

#### Validation
1. The system validates whether the required fields (username, email, password) are not empty.
1. The system validates whether the username and email are unique.
1. The system validates whether the email matches a regex for an email.
1. The system validates whether the passwords match and are of an acceptable complexity level.

### Welcome

#### Normal Flow
The system displays the user's first name if it exists, or username if it doesn't, and role.
The system displays an option to list all existing accounts if the user is an admin.

## Notes for TA
Th first name and last name fields in the register view is not validated.
This was a purposeful decision because they are not required fields and there are no "illegal" names.  

The system does not display the current accounts to the admin on the welcome screen to avoid cluttering the view,
but the accounts are displayed in a new view that can be opened from the welcome screen using the `viewAccountsBtn`.
