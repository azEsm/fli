package ml.fli.services;

import ml.fli.models.VkApiParams;

//TODO подумать, все ли эти методы нудны в API
public interface VkService {
    /**
     * Заполнение параметров для получения пользователя по id
     * @param userId
     * @return
     */
    String getUser(String userId);

    /**
     * Заполнение параметров для получения списка пользователя по заданным значениям
     * @param param
     * @return
     */
    String getUsersList(VkApiParams param);

    /**
     * Заполнение параметров для получения списка аудиозаписей пользователя по id
     * @param userId
     * @param count
     * @return
     */
    String getUserAudios(int userId, int count);

    /**
     * Заполнение параметров для получения списка групп пользователя по id
     * @param userId
     * @param count
     * @return
     */
    String getUserGroups(int userId, int count);

    /**
     * Вызов хранимой процедуры для получения 12 пользователей с аудиозаписями и группами
     * @return
     */
    String executeUsers();

    /**
     * Вызов хранимой процедуры для получения 12 пользователей с аудиозаписями и группами
     * @param listId
     * @return
     */
    String executeAudioAndGroup(String listId);
}
