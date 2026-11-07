# Démarrer le projet avec Docker

Ce guide explique comment lancer Cabinet Medical en local avec Docker Compose.

## Prérequis

- Docker Desktop installé et démarré.
- Le plugin Docker Compose disponible (`docker compose version`).
- Un terminal ouvert à la racine du projet, dans le dossier contenant `docker-compose.yml`.

## 1. Configurer les variables d'environnement

Compose lit le fichier `.env` à la racine du projet. Il doit contenir les variables suivantes :

```dotenv
JWT_SECRET=
POSTGRES_PASSWORD=
GATEWAY_PORT=8080
```

Le projet possède déjà un fichier `.env` : ouvrez-le et vérifiez que ces noms de variables sont présents, sans remplacer vos valeurs secrètes par celles d'un exemple. Si vous devez créer le fichier, vous pouvez partir du modèle :

```powershell
Copy-Item .env.example .env
```

Renseignez ensuite les valeurs dans `.env` :

- `JWT_SECRET` : clé aléatoire d'au moins 32 octets UTF-8. La même clé sert à signer et vérifier les jetons JWT dans les trois services.
- `POSTGRES_PASSWORD` : mot de passe robuste pour PostgreSQL.
- `GATEWAY_PORT` : port local d'accès au projet ; `8080` par défaut.

Ne publiez pas et ne partagez pas le fichier `.env`.

## 2. Construire et démarrer les services

Depuis la racine du projet, lancez :

```powershell
docker compose up --build -d
```

Au premier démarrage, Docker télécharge les images nécessaires et construit les images des services. PostgreSQL démarre d'abord ; les services applicatifs attendent que la base de données soit prête.

Vérifiez l'état des conteneurs :

```powershell
docker compose ps
```

Les services attendus sont `database`, `auth-service`, `patient-service`, `appointment-service` et `gateway`. La passerelle est accessible à l'adresse :

```text
http://localhost:8080
```

Si vous avez modifié `GATEWAY_PORT`, remplacez `8080` par la valeur configurée.

## 3. Vérifier les journaux

Afficher les journaux de tous les conteneurs :

```powershell
docker compose logs -f
```

Pour suivre un seul service, par exemple l'authentification :

```powershell
docker compose logs -f auth-service
```

Quittez l'affichage continu avec `Ctrl+C` ; cela n'arrête pas les conteneurs.

## 4. Tester l'API

Créer un compte et récupérer son jeton JWT avec PowerShell :

```powershell
$auth = Invoke-RestMethod -Method Post `
  -Uri http://localhost:8080/api/auth/register `
  -ContentType 'application/json' `
  -Body '{"email":"medecin@example.com","password":"ChangeMe123"}'

$token = $auth.accessToken
```

Les API patient et rendez-vous nécessitent l'en-tête `Authorization: Bearer <jeton>`. Par exemple, pour vérifier l'accès à la liste des patients :

```powershell
Invoke-RestMethod -Method Get `
  -Uri http://localhost:8080/api/patients `
  -Headers @{ Authorization = "Bearer $token" }
```

Les principales routes sont :

| Fonction | Méthode et route |
|---|---|
| Créer un compte | `POST /api/auth/register` |
| Se connecter | `POST /api/auth/login` |
| Gérer les patients | `/api/patients` |
| Gérer les rendez-vous | `/api/appointments` |

## 5. Arrêter le projet

Arrêter et supprimer les conteneurs et le réseau, tout en conservant les données PostgreSQL dans le volume Docker :

```powershell
docker compose down
```

Pour redémarrer ultérieurement :

```powershell
docker compose up -d
```

**Attention :** `docker compose down -v` supprime également le volume de la base de données et toutes les données qu'il contient. Ne l'utilisez que si vous souhaitez réellement réinitialiser la base.

## Dépannage rapide

- **Variable obligatoire manquante** : vérifiez que `.env` est à la racine et contient `JWT_SECRET` et `POSTGRES_PASSWORD` avec des valeurs non vides.
- **Port déjà utilisé** : changez `GATEWAY_PORT` dans `.env`, puis recréez les conteneurs avec `docker compose up --build -d`.
- **Un service ne démarre pas** : consultez ses journaux avec `docker compose logs <nom-du-service>` et vérifiez l'état avec `docker compose ps`.
