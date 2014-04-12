/************************************	*/
/* Percussion Piece	             	*/
/*	GUI							*/
/************************************	*/

(
var loadButton, stopButton, playButton, guiButton;
~path = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/PerC";

s.waitForBoot(

	w=Window("for putten; michael murphy 2012", Rect(100, 200, 290, 90));
	w.view.decorator = FlowLayout(w.view.bounds);
	w.view.decorator.gap = 10@10;
		
	loadButton = Button(w, 70 @ 20);
	loadButton.states = [["INIT",Color.black,Color.white]];
	loadButton.action = { |butt|
		thisProcess.interpreter.executeFile(~path ++ "/PerC_Startup.sc");
	};
	
	guiButton = Button(w, 70 @ 20);
	guiButton.states = [["PDEFGUI",Color.black,Color.white]];
	guiButton.action = { |butt|
		PdefAllGui(8);
	};
	
		w.view.decorator.nextLine;

	playButton = Button(w, 70 @ 20);
	playButton.states = [["PLAY",Color.black,Color.white]];
	playButton.action = { |butt|
		~sequencer_stream = ~sequencer.play;	
		postln("Play!");

	};
	
		w.view.decorator.nextLine;
		
	stopButton = Button(w, 70 @ 20);
	stopButton.states = [["STOP",Color.black,Color.white]];
	stopButton.action = { |butt|
		~stop_all.fork;
		postln("Stop!");
	};
	
	//Bring the window to the front
	w.front;

	//When this window closes, cleanup
	w.onClose = { 
		postln("Window Closed!");
		~stop_all.fork;
		~cleanup;
	};
)
)
