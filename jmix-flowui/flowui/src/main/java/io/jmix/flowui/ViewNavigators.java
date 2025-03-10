package io.jmix.flowui;

import com.vaadin.flow.component.HasValue;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.flowui.component.EntityPickerComponent;
import io.jmix.flowui.component.ListDataComponent;
import io.jmix.flowui.data.DataUnit;
import io.jmix.flowui.data.HasType;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.navigation.*;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkState;
import static io.jmix.core.common.util.Preconditions.checkNotNullArgument;

@Component("flowui_ViewNavigators")
public class ViewNavigators {

    protected DetailViewNavigationProcessor detailViewNavigationProcessor;
    protected ListViewNavigationProcessor listViewNavigationProcessor;
    protected ViewNavigationProcessor viewNavigationProcessor;

    public ViewNavigators(DetailViewNavigationProcessor detailViewNavigationProcessor,
                          ListViewNavigationProcessor listViewNavigationProcessor,
                          ViewNavigationProcessor viewNavigationProcessor) {
        this.detailViewNavigationProcessor = detailViewNavigationProcessor;
        this.listViewNavigationProcessor = listViewNavigationProcessor;
        this.viewNavigationProcessor = viewNavigationProcessor;
    }

    public <E> DetailViewNavigator<E> detailView(Class<E> entityClass) {
        checkNotNullArgument(entityClass);
        return new DetailViewNavigator<>(entityClass, detailViewNavigationProcessor::processNavigation);
    }

    public <E> DetailViewNavigator<E> detailView(Class<E> entityClass, View<?> parent) {
        checkNotNullArgument(entityClass);
        return detailView(entityClass)
                .withBackNavigationTarget(parent.getClass());
    }

    public <E> DetailViewNavigator<E> detailView(ListDataComponent<E> listDataComponent) {
        checkNotNullArgument(listDataComponent);

        Class<E> beanType = getBeanType(listDataComponent);

        DetailViewNavigator<E> navigator =
                new DetailViewNavigator<>(beanType, detailViewNavigationProcessor::processNavigation);

        E selected = listDataComponent.getSingleSelectedItem();
        if (selected != null) {
            navigator.editEntity(selected);
        }

        return navigator;
    }

    public <E> DetailViewNavigator<E> detailView(ListDataComponent<E> listDataComponent, View<?> parent) {
        return detailView(listDataComponent)
                .withBackNavigationTarget(parent.getClass());
    }

    public <E> DetailViewNavigator<E> detailView(EntityPickerComponent<E> picker) {
        checkNotNullArgument(picker);
        checkState(picker instanceof HasValue,
                "A component must implement " + HasValue.class.getSimpleName());

        Class<E> beanType = getBeanType(picker);

        DetailViewNavigator<E> navigator =
                new DetailViewNavigator<>(beanType, detailViewNavigationProcessor::processNavigation);

        //noinspection unchecked
        E value = ((HasValue<?, E>) picker).getValue();
        if (value != null) {
            navigator.editEntity(value);
        }

        return navigator;
    }

    public <E> DetailViewNavigator<E> detailView(EntityPickerComponent<E> picker, View<?> parent) {
        return detailView(picker)
                .withBackNavigationTarget(parent.getClass());
    }

    public <E> ListViewNavigator<E> listView(Class<E> entityClass) {
        checkNotNullArgument(entityClass);

        return new ListViewNavigator<>(entityClass, listViewNavigationProcessor::processNavigation);
    }

    public ViewNavigator view(Class<? extends View> viewClass) {
        return new ViewNavigator(viewNavigationProcessor::processNavigation)
                .withViewClass(viewClass);
    }

    public ViewNavigator view(String viewId) {
        return new ViewNavigator(viewNavigationProcessor::processNavigation)
                .withViewId(viewId);
    }

    protected <E> Class<E> getBeanType(ListDataComponent<E> listDataComponent) {
        DataUnit items = listDataComponent.getItems();
        if (items instanceof HasType) {
            //noinspection unchecked
            return ((HasType<E>) items).getType();
        } else {
            throw new IllegalStateException(String.format("Component '%s' is not bound to data " +
                    "or unable to determine type of items", listDataComponent));
        }
    }

    protected <E> Class<E> getBeanType(EntityPickerComponent<E> picker) {
        MetaClass metaClass = picker.getMetaClass();
        if (metaClass == null) {
            throw new IllegalStateException(String.format("Component '%s' is not bound to data " +
                    "or unable to determine type of items", picker));
        }

        return metaClass.getJavaClass();
    }
}
