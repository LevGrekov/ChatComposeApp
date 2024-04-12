package ru.levgrekov.chat.net

import java.security.MessageDigest
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

object DbContext {
    private const val URL: String = "jdbc:postgresql://localhost:5432/Chat"
    private const val USERNAME: String = "postgres"
    private const val PASSWORD: String = "1937"

    private fun getConnection(): Connection =
        DriverManager.getConnection(URL, USERNAME, PASSWORD)

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hashedBytes)
    }



    fun authenticateUser(login: String, password: String): Pair<String,Int> {
        var result = Pair("Пользователь $login не найден",1)
        getConnection().use { connection ->
            try {
                val sql = "SELECT password FROM user_account WHERE login = ?"
                connection.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, login)
                    val resultSet = stmt.executeQuery()
                    if (resultSet.next()) {
                        val hashedPassword = resultSet.getString("password")
                        if (hashedPassword == hashPassword(password)) {
                            result = Pair("Вы успешно авторизовались",0)
                        }
                        else {
                            result = Pair("Неверный Пароль",2)
                        }
                    }
                }
            }
            catch (e: SQLException) {
                result = Pair("Ошбка подключения к БД",3)
                e.printStackTrace()
            }
        }
        return result
    }

    fun registerUser(login: String, password: String) : Pair<String,Int> {
        var result: Pair<String,Int>
        var exists = false
        try {
            getConnection().use {
                val sqlCheckExist = "SELECT COUNT(*) FROM user_account WHERE login = ?"
                it.prepareStatement(sqlCheckExist).use { statement ->
                    statement.setString(1, login)
                    val resultSet = statement.executeQuery()
                    if (resultSet.next()) {
                        val count = resultSet.getInt(1)
                        exists = (count == 1)
                    }
                    result = Pair("Пользователь с таким именем уже существует",1)
                }
                if(!exists){
                    val sqlAdd = "INSERT INTO user_account (login, password) VALUES (?, ?)"
                    it.prepareStatement(sqlAdd).use { stmt->
                        stmt.setString(1, login)
                        stmt.setString(2, hashPassword(password))
                        stmt.executeUpdate()
                    }
                    result = Pair("Вы успешно Зарегистрировались",0)
                }
            }
        }
        catch (e: SQLException) {
            result = Pair("Ошбка подключения к БД",2)
            e.printStackTrace()
        }
        return result
    }
}