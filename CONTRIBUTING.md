# LuckyGame: правила работы команды

## Ветки
- `main` защищена: прямой push запрещён, только через Pull Request.
- Новая работа ведётся в ветке от `main`:
    - `feature/entities` (samurai)
    - `feature/services-dto` (dobryak)
    - `feature/controllers` (sawo)
    - `feature/exception-handling` (sawo)
- Имя ветки: `feature/<что-делаете>`, строчными, через дефис.

## Перед началом работы
1. `git checkout main`
2. `git pull origin main`
3. `git checkout -b feature/<название>`

## Commit
- Коммит маленький и про одно дело.
- Сообщение в настоящем времени: `Add Product entity`, а не `added`.

## Pull Request
- Открывайте PR в `main`, опишите, что сделано и как проверить.
- Перед PR: проект собирается (`Build → Rebuild Project`) и запускается без ошибок.
- Минимум один апрув от другого участника. Себе не апрувьте.
- После мержа удаляйте ветку.

## Договорённости по коду
- Пакеты: `entity`, `repository`, `dto`, `service`, `controller`, `exception`.
- Имена сущностей, полей и эндпоинтов берутся из таблицы ниже, без самодеятельности.
- Kotlin: `data class` для DTO, `val` по умолчанию, `var` только когда нужно.
- Не коммитьте `application.properties` с личными паролями. Если нужны свои настройки, используйте переменные окружения.
- Не коммитьте `target/`, `.idea/`, логи.

## Изменение контракта
Если нужно поменять имя поля или эндпоинт, сначала напишите в чат команды, потом меняйте в `README` и в коде одним PR.

## Запуск проекта
1. `docker compose up -d` (или команда `docker run` из README, если compose не установлен).
2. Запустить `LuckygameApplication` в IDEA.
3. Приложение на `http://localhost:8081`.

## Контракт
(здесь таблица сущностей, DTO и эндпоинтов, которую согласуете в команде)