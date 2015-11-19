package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;
import org.cyk.utility.common.generator.RandomDataProvider;

@Getter @Setter @Named @ViewScoped //TOFIX to be moved on GUI projects
public class SampleDataPage extends AbstractPrimefacesPage implements Serializable {

	private static final long serialVersionUID = 3459311493291130244L;

	@Inject private RootRandomDataProvider rootRandomDataProvider;
    
	private RootBusinessLayer rootBusinessLayer = RootBusinessLayer.getInstance();
	private RandomDataProvider randomDataProvider = RandomDataProvider.getInstance();

	private Data data = new Data();
	private FormOneData<Data> form;
		
	@Override
	protected void initialisation() {
		super.initialisation();
		form = (FormOneData<Data>) createFormOneData(data, Crud.CREATE);
		form.setDynamic(Boolean.TRUE);
		form.getSubmitCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = -4051260187568895766L;
			@Override
			public void serve(UICommand command, Object parameter) {
				run();
			}
		});
	}
	
	public void run(){
		switch(data.getAction()){
		
		}
	}
	
	
	
	@Getter @Setter
	public class Data implements Serializable{
		private static final long serialVersionUID = 7484994571511058040L;
		
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull
		private Action action;
		
	}
	
	
	
}
