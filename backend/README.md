# NTD Calculator Backend

## Users

- [x] provide with at least two user/passwords


- `john@gmail.com` password `john0000`
- `mary@gmail.com` password `mary0000`

## Tech Stack, choose from: Java, Clojure, MySQL, Node.js, Go, Python, Vue.js/React.js, AWS.

- [x] Java (Spring Boot/Maven)
- [x] MySQL

## Repos

- [x] Frontend and Backend should be on separate stacks.
- [x] Frontend and Backend should be on separate repos.

## Technical requirements

### Backend

- [x] all client-server interaction should be through RESTful API (versionated).
- [x] collection endpoints should be able to provide filters and pagination. 
- [x] operations should be able to handle negative numbers. For example (-1) - (-5) = 4, YES but no parenthesis
- [x] use third-party operation for random string https://www.random.org/clients, YES 8 digits (hardcoded size)
- [x] decide the cost of each operation, YES defined at startup (hardcoded)
- [x] users will have a starting credit/balance, YES defined at startup currently set to 100 (hardcoded, there is one record present with this balance)
- [x] all the database operations should be transactional, to maintain integrity when multiple operations try to modify the balance at the same time.
- [x] the system does not allow more operations if there is not enough balance. YES per USER (multiple logins are allowed), the balance is not shared
- [x] each request will be deducted from the user’s balance. If the user’s balance isn’t enough to cover the request cost, the request shall be denied.
- [x] records should be soft-deleted only (the arithmetic operations should be able to be deleted with a logic delete in the database)
- [x] all the logic should be isolated in the service layer, never write logic in the controllers.
- [x] include a log system.
- [x] the passwords should be encrypted.
- [x] automated tests such as Unit Tests back-end

## Other

- form-based authentication (obtains a token)
- token-based authorization (api requests require a valid token), expires in 10 hours (hardcoded)
- API documentation in Calculator_REST_API.pdf or in the `target/site` folder after running `doc.sh` (parent folder, also available in `/api-docs` after authentication)

## Improvements (unspecified, need more time)

[ ] setup users & operations via configuration, move hardcoded configuration to application.properties
[ ] use a Vault for storing secrets
[ ] revoke token after logout
