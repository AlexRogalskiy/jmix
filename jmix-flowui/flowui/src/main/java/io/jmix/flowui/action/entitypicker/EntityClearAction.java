package io.jmix.flowui.action.entitypicker;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.model.MetaProperty;
import io.jmix.flowui.FlowuiComponentProperties;
import io.jmix.flowui.action.ActionType;
import io.jmix.flowui.action.valuepicker.PickerAction;
import io.jmix.flowui.data.EntityValueSource;
import io.jmix.flowui.data.ValueSource;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.EntityPickerComponent;
import io.jmix.flowui.kit.component.FlowuiComponentUtils;
import io.jmix.flowui.kit.component.KeyCombination;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.UiControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkState;
import static io.jmix.flowui.component.UiComponentUtils.getEmptyValue;

@ActionType(EntityClearAction.ID)
public class EntityClearAction<E> extends PickerAction<EntityClearAction<E>, EntityPickerComponent<E>, E> {

    public static final String ID = "entity_clear";

    public EntityClearAction() {
        this(ID);
    }

    public EntityClearAction(String id) {
        super(id);
    }

    @Override
    protected void initAction() {
        super.initAction();

        this.icon = FlowuiComponentUtils.convertToIcon(VaadinIcon.CLOSE);
    }

    @Autowired
    public void setMessages(Messages messages) {
        this.text = messages.getMessage("actions.valuePicker.clear.description");
    }

    @Autowired
    protected void setFlowUiComponentProperties(FlowuiComponentProperties flowUiComponentProperties) {
        this.shortcutCombination = KeyCombination.create(flowUiComponentProperties.getPickerClearShortcut());
    }

    @Override
    public void setTarget(@Nullable EntityPickerComponent<E> target) {
        checkState(target instanceof HasValue,
                "A component must implement " + HasValue.class.getSimpleName());

        super.setTarget(target);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute() {
        ValueSource<E> valueSource = target.getValueSource();
        HasValue<?, E> hasValue = (HasValue<?, E>) target;

        if (!hasValue.isEmpty()
                && valueSource instanceof EntityValueSource) {
            EntityValueSource<?, ?> entityValueSource = (EntityValueSource<?, ?>) valueSource;
            entityValueSource.getMetaPropertyPath();
            if (entityValueSource.getMetaPropertyPath().getMetaProperty().getType() == MetaProperty.Type.COMPOSITION) {
                View view = UiComponentUtils.findView(((Component) target));
                if (view != null) {
                    DataContext dataContext = UiControllerUtils.getViewData(view).getDataContext();
                    dataContext.remove(hasValue.getValue());
                }
            }
        }

        target.setValueFromClient(getEmptyValue((Component) target));
    }
}
