
package harshbarash.github.monetaru.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

//Простое представление, обеспечивающее обратный вызов рендеринга для других классов.
public class OverlayView extends View {
  private final List<DrawCallback> callbacks = new LinkedList<DrawCallback>();

  public OverlayView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public void addCallback(final DrawCallback callback) {
    callbacks.add(callback);
  }

  @Override
  public synchronized void draw(final Canvas canvas) {
    super.draw(canvas);
    super.draw(canvas);
    for (final DrawCallback callback : callbacks) {
      callback.drawCallback(canvas);
    }
  }

  //Интерфейс, определяющий обратный вызов для клиентских классов
  public interface DrawCallback {
    public void drawCallback(final Canvas canvas);
  }
}
