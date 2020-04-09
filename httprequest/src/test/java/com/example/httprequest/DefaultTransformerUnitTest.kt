package com.example.httprequest

import org.junit.Test

import org.junit.Assert.*

/**
 * 默认数据格式转换器测试.
 */
class DefaultTransformerUnitTest {

    data class UserData(val name: String, val age: Int)

    /**
     * 测试默认数据转换器
     */
    @Test
    fun testApiDataDefaultTransformer() {
        val transformer = ApiDataDefaultTransformer()

        // 一个成功的响应测试，空数据返回
        assert(
            transformer.transform(
                body = """
                    {
                        "result": true,
                        "message": "success",
                        "data" : null
                    }
                """,
                code = 200
            ).isOk()
        )

        // 一个错误的响应格式
        assertFalse(
            transformer.transform(
                body = """
                    {
                        "result": true,
                        "message": "success",
                        "data" : null
                    // 这里缺失了内容
                """,
                code = 200
            ).isOk()
        )

        // 对象数据返回
        val user = transformer.transform(
            body = """
                    {
                        "result": true,
                        "message": "success",
                        "data" : {
                            "name" : "Jack",
                            "age" : 18
                        }
                    }
                """,
            code = 200
        ).getObjectData(UserData::class.java)!!
        assertEquals(user.name, "Jack")
        assertEquals(user.age, 18)

        // 分页数据返回
        val pageData = transformer.transform(
            body = """
                    {
                        "result": true,
                        "message": "success",
                        "data" : {
                            "total" : 10,
                            "rows" : [
                                { "name" : "Rose", "age" : 16 },
                                { "name" : "Jim", "age" : 17 },
                                { "name" : "Jack", "age" : 18 }
                            ]
                        }
                    }
                """,
            code = 200
        ).getPageData(UserData::class.java)
        assertEquals(pageData.total, 10)
        assertEquals(pageData.rows.size, 3)
        assertEquals(pageData.rows[2].name, "Jack")
        assertEquals(pageData.rows[2].age, 18)
    }
}
