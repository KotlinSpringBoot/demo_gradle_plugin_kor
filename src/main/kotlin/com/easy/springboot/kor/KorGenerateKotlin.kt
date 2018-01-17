package com.easy.springboot.kor

import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class KorGenerateKotlin {
    val datestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    val comment = """/**
     * Created by Kor on ${datestamp}. author: 东海陈光剑
     */"""

    fun doGenerate(projectDir: String, packageName: String, entityName: String) {
        val packagePath = getPackagePath(packageName)

        val targetEntityDir = "$projectDir/src/main/kotlin/$packagePath/entity/"
        val targetDaoDir = "$projectDir/src/main/kotlin/$packagePath/dao/"
        val targetServiceDir = "$projectDir/src/main/kotlin/$packagePath/service/"
        val targetServiceImplDir = "$projectDir/src/main/kotlin/$packagePath/service/impl/"
        val targetControllerDir = "$projectDir/src/main/kotlin/$packagePath/controller/"

        generateEntity(targetEntityDir, entityName, packageName)
        generateDao(targetDaoDir, entityName, packageName)
        generateService(targetServiceDir, entityName, packageName)
        generateServiceImpl(targetServiceImplDir, entityName, packageName)
        generateController(targetControllerDir, entityName, packageName)
    }

    private fun generateServiceImpl(targetServiceImplDir: String, entityName: String, packageName: String) {

        val f = File(targetServiceImplDir)

        if (!f.exists()) {
            println("Create Dir: $targetServiceImplDir")
            f.mkdirs()
        }

        val srcFileName = "$targetServiceImplDir${entityName}ServiceImpl.kt"

        val srcFile = File(srcFileName)
        if (!srcFile.exists()) {
            println("Create File: $srcFileName")
            srcFile.createNewFile()
        }
        val text = serviceImplTemplate(entityName, packageName)
        println("Write Text:\n $text")
        srcFile.writeText(text)
    }

    private fun generateService(targetServiceDir: String, entityName: String, packageName: String) {

        val f = File(targetServiceDir)

        if (!f.exists()) {
            println("Create Dir: $targetServiceDir")
            f.mkdirs()
        }

        val srcFileName = "$targetServiceDir${entityName}Service.kt"

        val srcFile = File(srcFileName)
        if (!srcFile.exists()) {
            println("Create File: $srcFileName")
            srcFile.createNewFile()
        }
        val text = serviceTemplate(entityName, packageName)
        println("Write Text:\n $text")
        srcFile.writeText(text)
    }


    private fun generateEntity(targetEntityDir: String, entityName: String, packageName: String) {

        val f = File(targetEntityDir)

        if (!f.exists()) {
            println("Create Dir: $targetEntityDir")
            f.mkdirs()
        }

        val srcFileName = "$targetEntityDir$entityName.kt"

        val srcFile = File(srcFileName)
        if (!srcFile.exists()) {
            println("Create File: $srcFileName")
            srcFile.createNewFile()
        }
        val text = entityTemplate(entityName, packageName)
        println("Write Text:\n $text")
        srcFile.writeText(text)
    }


    private fun generateDao(targetDaoDir: String, entityName: String, packageName: String) {

        val f = File(targetDaoDir)

        if (!f.exists()) {
            println("Create Dir: $targetDaoDir")
            f.mkdirs()
        }

        val srcFileName = "$targetDaoDir${entityName}Dao.kt"

        val srcFile = File(srcFileName)
        if (!srcFile.exists()) {
            println("Create File: $srcFileName")
            srcFile.createNewFile()
        }
        val text = daoTemplate(entityName, packageName)
        println("Write Text:\n $text")
        srcFile.writeText(text)

    }

    private fun generateController(targetControllerDir: String, entityName: String, packageName: String) {
        val f = File(targetControllerDir)

        if (!f.exists()) {
            println("Create Dir: $targetControllerDir")
            f.mkdirs()
        }

        val srcFileName = "$targetControllerDir${entityName}Controller.kt"

        val srcFile = File(srcFileName)
        if (!srcFile.exists()) {
            println("Create File: $srcFileName")
            srcFile.createNewFile()
        }

        val text = controllerTemplate(entityName, packageName)
        println("Write Text:\n $text")
        srcFile.writeText(text)

    }


    /**
     * 把包名替换为路径名：例如com.easykotlin.reakt --> com/easykotlin/reakt
     */
    private fun getPackagePath(packageName: String): String {
        return packageName.replace(".", "/")
    }


    private fun entityTemplate(entityName: String, packageName: String): String {
        return """
package ${packageName}.entity
import javax.persistence.*
import java.util.Date
${comment}
@Entity
class ${entityName} {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1
    var gmtCreate = Date()
    var gmtModify = Date()
    var isDeleted = 0
}
"""

    }

    private fun controllerTemplate(entityName: String, packageName: String): String {
        return """
package ${packageName}.controller
import ${packageName}.dao.${entityName}Dao
import ${packageName}.entity.${entityName}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import javax.servlet.http.HttpServletRequest
${comment}
@RestController
@RequestMapping("/${entityName.toLowerCase()}")
class ${entityName}Controller {
    @Autowired lateinit var ${entityName}Dao: ${entityName}Dao
    @GetMapping(value = ["/"])
    fun ${entityName.toLowerCase()}(request: HttpServletRequest): List<${entityName}>{
        return ${entityName}Dao.findAll()
    }
    @GetMapping(value = ["/{id}"])
    fun getOne(@PathVariable("id") id:Long): ${entityName} {
        return ${entityName}Dao.getOne(id)
    }
}
"""

    }

    private fun daoTemplate(entityName: String, packageName: String): String {
        return """
package ${packageName}.dao
import ${packageName}.entity.${entityName}
import org.springframework.data.jpa.repository.JpaRepository
${comment}
interface ${entityName}Dao : JpaRepository<${entityName}, Long> {
}
"""

    }

    private fun serviceTemplate(entityName: String, packageName: String): String {

        return """
package ${packageName}.service
${comment}
interface ${entityName}Service {
}
"""
    }

    private fun serviceImplTemplate(entityName: String, packageName: String): String {
        return """
package ${packageName}.service.impl

import ${packageName}.service.${entityName}Service

${comment}
class ${entityName}ServiceImpl:  ${entityName}Service{
}
"""

    }


}
