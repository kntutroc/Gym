package programacion.kimberly.gym

import android.content.Intent
import android.widget.Button
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import programacion.kimberly.gym.register.MainActivity
import programacion.kimberly.gym.ui.bar.SettingsActivity

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@LooperMode(LooperMode.Mode.PAUSED)
class SettingsActivityTest {

    private lateinit var activity: SettingsActivity

    @Before
    fun setUp() {
        // Inicializar la actividad bajo prueba
        activity = Robolectric.buildActivity(SettingsActivity::class.java).create().resume().get()
    }

    @After
    fun tearDown() {
        // Finalizar la actividad después de cada prueba
        activity.finish()
    }

    @Test
    fun testSignOutButton() {
        // Verificar que la actividad se haya iniciado correctamente
        assertNotNull(activity)

        // Realizar clic en el botón de cerrar sesión
        activity.findViewById<Button>(R.id.buttonSignOut).performClick()

        // Verificar que se redirige a MainActivity después de cerrar sesión
        val expectedIntent = Intent(activity, MainActivity::class.java)
        val actualIntent = Shadows.shadowOf(activity).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)
    }
}
