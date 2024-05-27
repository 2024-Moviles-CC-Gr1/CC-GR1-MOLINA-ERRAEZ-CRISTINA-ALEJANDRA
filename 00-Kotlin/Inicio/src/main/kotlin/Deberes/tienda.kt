import javax.swing.JOptionPane
import java.io.File
import java.io.IOException

// Definimos la clase Tienda
class Tienda(
    val nombre: String,
    val ubicacion: String,
    val telefono: String,
    var abierta: Boolean,
    var horario: String
) {
    val productos = mutableListOf<Producto>()

    // Agregar producto a la tienda
    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    // Obtener la lista de productos de la tienda
    fun obtenerProductos(): List<Producto> {
        return productos.toList()
    }

    // Eliminar producto de la tienda
    fun eliminarProducto(index: Int) {
        if (index in 0 until productos.size) {
            productos.removeAt(index)
        }
    }

    // Editar producto de la tienda
    fun editarProducto(index: Int, producto: Producto) {
        if (index in 0 until productos.size) {
            productos[index] = producto
        }
    }

    // Guardar la información de la tienda y sus productos en archivos de texto
    fun guardarEnArchivo() {
        try {
            // Guardar información de la tienda
            val tiendaFile = File("$nombre.txt")
            tiendaFile.bufferedWriter().use { out ->
                out.write("Nombre: $nombre\n")
                out.write("Ubicación: $ubicacion\n")
                out.write("Teléfono: $telefono\n")
                out.write("Abierta: $abierta\n")
                out.write("Horario: $horario\n")
            }

            // Guardar información de los productos
            val productosFile = File("$nombre-productos.txt")
            productosFile.bufferedWriter().use { out ->
                productos.forEach { producto ->
                    out.write("Nombre: ${producto.nombre}\n")
                    out.write("Cantidad: ${producto.cantidad}\n")
                    out.write("Precio: ${producto.precio}\n")
                    out.write("Disponible: ${producto.esDisponible}\n")
                    out.write("Fecha de expiración: ${producto.fechaExpiracion}\n")
                    out.write("\n")
                }
            }
            println("Información de la tienda guardada en archivos.")
        } catch (e: IOException) {
            println("Error al guardar la información en archivos.")
            e.printStackTrace()
        }
    }
}

// Definimos la clase Producto
data class Producto(
    var nombre: String,
    var cantidad: Int,
    var precio: Double,
    var esDisponible: Boolean,
    var fechaExpiracion: String
)

fun main() {
    // Crear la tienda
    val tienda = Tienda("CrisViveres", "Sangolquí", "0959610759", true, "09:00 - 18:00")

    // Mostrar el menú principal y manejar la interacción con el usuario
    manejarMenu(tienda)

    // Guardar la información de la tienda y sus productos en archivos de texto
    tienda.guardarEnArchivo()
}

// Función para manejar el menú principal y la interacción con el usuario
fun manejarMenu(tienda: Tienda) {
    var opcion: Int

    // Bucle del menú principal
    do {
        // Mostrar el menú principal
        opcion = JOptionPane.showInputDialog(
            null,
            "Bienvenido/a al sistema de gestión de la tienda CrisViveres\n" +
                    "Elige una opción:\n" +
                    "1) Añadir producto\n" +
                    "2) Ver productos\n" +
                    "3) Editar producto\n" +
                    "4) Eliminar producto\n" +
                    "5) Cambiar estado de la tienda\n" +
                    "6) Salir"
        )?.toIntOrNull() ?: 0

        when (opcion) {
            1 -> crearProducto(tienda)
            2 -> verProductos(tienda)
            3 -> editarProducto(tienda)
            4 -> eliminarProducto(tienda)
            5 -> cambiarEstadoTienda(tienda)
            6 -> JOptionPane.showMessageDialog(null, "¡Hasta luego!")
            else -> JOptionPane.showMessageDialog(null, "Opción inválida.")
        }
    } while (opcion != 6)
}

// Función para crear un nuevo producto
fun crearProducto(tienda: Tienda) {
    val nombre = JOptionPane.showInputDialog("Introduce el nombre del producto:")
    val cantidad = JOptionPane.showInputDialog("Introduce la cantidad:")
    val precio = JOptionPane.showInputDialog("Introduce el precio:")
    val esDisponible = JOptionPane.showInputDialog("El producto está disponible? (true/false):")?.toBoolean()
    val fechaExpiracion = JOptionPane.showInputDialog("Introduce la fecha de expiración (DD-MM-YYYY):")

    tienda.agregarProducto(
        Producto(
            nombre ?: "",
            cantidad?.toIntOrNull() ?: 0,
            precio?.toDoubleOrNull() ?: 0.0,
            esDisponible ?: false,
            fechaExpiracion ?: ""
        )
    )

    JOptionPane.showMessageDialog(null, "Producto creado correctamente.")
}

// Función para ver los productos de la tienda
fun verProductos(tienda: Tienda) {
    val sb = StringBuilder()
    sb.append("Productos en ${tienda.nombre}:\n")
    tienda.obtenerProductos().forEachIndexed { index, producto ->
        sb.append("${index + 1}) ${producto.nombre} ,Cantidad: ${producto.cantidad}\n")
    }
    JOptionPane.showMessageDialog(null, sb.toString())
}

// Función para editar un producto de la tienda
fun editarProducto(tienda: Tienda) {
    if (tienda.productos.isNotEmpty()) {
        val sb = StringBuilder()
        sb.append("Elige el producto a editar:\n")
        tienda.obtenerProductos().forEachIndexed { index, producto ->
            sb.append("${index + 1}) ${producto.nombre}\n")
        }
        val index = JOptionPane.showInputDialog(null, sb.toString())?.toIntOrNull()?.minus(1)
        if (index != null && index in 0 until tienda.productos.size) {
            val producto = tienda.productos[index]
            val nuevoNombre = JOptionPane.showInputDialog("Introduce el nuevo nombre del producto:", producto.nombre)
            val nuevaCantidad = JOptionPane.showInputDialog("Introduce la nueva cantidad:", producto.cantidad)
            val nuevoPrecio = JOptionPane.showInputDialog("Introduce el nuevo precio:", producto.precio)
            val nuevaDisponibilidad = JOptionPane.showInputDialog("El producto está disponible? (true/false):", producto.esDisponible.toString())?.toBoolean()
            val nuevaFechaExpiracion = JOptionPane.showInputDialog("Introduce la nueva fecha de expiración (YYYY-MM-DD):", producto.fechaExpiracion)

            tienda.editarProducto(
                index,
                Producto(
                    nuevoNombre ?: producto.nombre,
                    nuevaCantidad?.toIntOrNull() ?: producto.cantidad,
                    nuevoPrecio?.toDoubleOrNull() ?: producto.precio,
                    nuevaDisponibilidad ?: producto.esDisponible,
                    nuevaFechaExpiracion ?: producto.fechaExpiracion
                )
            )

            JOptionPane.showMessageDialog(null, "Producto editado correctamente.")
        } else {
            JOptionPane.showMessageDialog(null, "Índice inválido.")
        }
    } else {
        JOptionPane.showMessageDialog(null, "No hay productos para editar.")
    }
}

// Función para eliminar un producto de la tienda
fun eliminarProducto(tienda: Tienda) {
    if (tienda.productos.isNotEmpty()) {
        val sb = StringBuilder()
        sb.append("Elige el producto a eliminar:\n")
        tienda.obtenerProductos().forEachIndexed { index, producto ->
            sb.append("${index + 1}) ${producto.nombre}\n")
        }
        val index = JOptionPane.showInputDialog(null, sb.toString())?.toIntOrNull()?.minus(1)
        if (index != null && index in 0 until tienda.productos.size) {
            tienda.eliminarProducto(index)
            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.")
        } else {
            JOptionPane.showMessageDialog(null, "Índice inválido.")
        }
    } else {
        JOptionPane.showMessageDialog(null, "No hay productos para eliminar.")
    }
}

// Función para cambiar el estado de la tienda
fun cambiarEstadoTienda(tienda: Tienda) {
    tienda.abierta = !tienda.abierta
    JOptionPane.showMessageDialog(
        null,
        if (tienda.abierta) "La tienda está abierta." else "La tienda está cerrada."
    )
}
