# RecycleApp

Приложение для добавления и отображения пунктов приёма перерабатываемых отходов. 
Поддерживает авторизацию, работу с картой и классификацию мусора с помощью модели машинного обучения.

## Функциональность

- Определение категории отхода по его изображению (картон, стекло, металл, бумага, пластик)
- Карта пунктов приёма с возможностью фильтрации по категории мусора 
- Авторизация по почте и паролю
- Добавление пунктов приёма отходов для авторизированных пользователей

Этот проект использует модель для классификации отходов на основе датасета [Trashnet](https://www.kaggle.com/datasets/feyzazkefe/trashnet/data) 
из работы [Pruning and Quantization in Keras](https://www.kaggle.com/code/sumn2u/pruning-and-quantization-in-keras/notebook), лицензия [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

## Скриншоты
### Авторизация

![Авторизация](screenshots/login.png)

### Регистрация

![Регистрация](screenshots/register.png)

### Карта

![Карта](screenshots/map.png)


![Фильтр](screenshots/filtermap.png)

### Управление пунктами

![Управление пунктами](screenshots/editpoint.png)

### Классификация отходов


![Классификация отходов](screenshots/classifier1.png)


![Классификация отходов](screenshots/classifier2.png)


### Запись работы
<details> <summary>Запись работы</summary>

![Запись работы](screenshots/screen.mp4)

</details>

## Установка

Скачать APK по [ccылке](https://github.com/ArtemPolushin/RecycleApp/releases/download/v1.0.0/app.apk)