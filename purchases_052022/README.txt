Инструкция по запуску:
1) Запустить БД в докере: выполнить файл runPg.src из папки docker
2) Выполнить команду для получения данных по критериям поиска:
    java -jar purchases_052022-1.0-SNAPSHOT.jar search data/inputSearch.json data/outputSearch.json
3) Выполнить команду для получения данных статистики:
    java -jar purchases_052022-1.0-SNAPSHOT.jar stat data/inputStat.json data/outputStat.json
4) Входные файлы для запуска находятся в папке data.
