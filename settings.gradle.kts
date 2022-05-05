include(
    ":app",
    ":launcher",
    ":module-plugin",
    ":agp-asm-plugin",
    ":service",
    ":common",
    ":shop",
    ":user",
    ":interfaces",
    ":compiler",
    ":ksp-compiler",
    ":moduleLike",
    ":base-plugin",
    ":home"
)
project(":home").projectDir = File("Business/home")
project(":service").projectDir =  File("Business/service")
project(":shop").projectDir =  File("Business/shop")
project(":user").projectDir =  File("Business/user")
project(":module-plugin").projectDir =  File("Plugin/module-plugin")
project(":agp-asm-plugin").projectDir =  File("Plugin/agp-asm-plugin")
project(":compiler").projectDir =  File("Plugin/compiler")
project(":ksp-compiler").projectDir =  File("Plugin/ksp-compiler")
project(":base-plugin").projectDir =  File("Plugin/base-plugin")
project(":interfaces").projectDir =  File("Lib/interfaces")
project(":launcher").projectDir =  File("Lib/launcher")
project(":moduleLike").projectDir =  File("Lib/moduleLike")

rootProject.name = "卷王"

