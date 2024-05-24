package programacion.kimberly.gym

import android.widget.Button
import android.widget.EditText
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FirstActivityTest {

    private lateinit var activity: FirstActivity

    @Before
    fun setUp() {
        // Inicializar la actividad bajo prueba
        activity = Robolectric.buildActivity(FirstActivity::class.java).create().resume().get()
    }

    @After
    fun tearDown() {
        // Finalizar la actividad después de cada prueba
        activity.finish()
    }

    @Test
    fun testShowChatDialog() {
        // Verificar que la actividad se haya iniciado correctamente
        assertNotNull(activity)

        // Realizar clic en el botón FAB para mostrar el diálogo de chat
        activity.showChatDialog()

        // Verificar que el diálogo se muestra correctamente
        val dialog = ShadowDialog.getLatestDialog()
        assertNotNull(dialog)

        // Simular la interacción con el diálogo
        val nameEditText = dialog.findViewById<EditText>(R.id.editTextName)
        val emailEditText = dialog.findViewById<EditText>(R.id.editTextEmail)
        val messageEditText = dialog.findViewById<EditText>(R.id.editTextMessage)

        nameEditText.setText("Test Name")
        emailEditText.setText("test@example.com")
        messageEditText.setText("Test Message")

        // Obtener el botón "Send" y simular un clic
        val sendButton = dialog.findViewById<Button>(android.R.id.button1)
        sendButton.performClick()

    }
}
