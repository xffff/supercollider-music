/************************************	*/
/* Ensemble Piece	      	       	*/
/*	GUI							*/
/************************************	*/

(
var initButton, stopButton, playButton, ctButton, routeButton;
~path = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/Ensemble_Piece";
~contimbre_path = "/Volumes/Time Machine Backups/conTimbre";

	w=Window(" ", Rect(100, 200, 85, 160));
	w.view.decorator = FlowLayout(w.view.bounds);
	w.view.decorator.gap = 10@10;
		
	initButton = Button(w, 70 @ 20);
	initButton.states = [["init",Color.black,Color.white]];
	initButton.action = { |butt|
		fork{
			("jackd -d coreaudio -r 48000 -p 1024").runInTerminal;
			2.wait;
			"open /Applications/Jack/JackPilot.app".unixCmd;
			2.wait;
			thisProcess.interpreter.executeFile(~path ++ "/Ens_Startup.sc");
			postln("Compiled... starting scsynth...");
			~startup.fork;
		}
	};
	
		w.view.decorator.nextLine;

	
	ctButton = Button(w, 70 @ 20);
	ctButton.states = [["cT init",Color.black,Color.white]];
	ctButton.action = { |butt|
		~contimbre_initialise.fork;
	};
	
		w.view.decorator.nextLine;

	routeButton = Button(w, 70 @ 20);
	routeButton.states = [["route audio",Color.black,Color.white]];
	routeButton.action = { |butt|
		fork{
			postln("Routing audio...");
			for(1,16, { |i, j| 
				// first connect MaxMSP to Reaper
				i=i+1; // start from 1 not 0
				("jack_connect MaxMSP:out"++i++" "++"REAPER:in"++i).systemCmd;
				
				// connect MaxMSP to scsynth
				("jack_connect MaxMSP:out"++i++" "++"scsynth:in"++i).systemCmd;
				
				j=j+17; // fx in is reaper channels 17-32
				// connect scsynth to Reaper
				("jack_connect scsynth:out"++i++" "++"REAPER:in"++j).systemCmd;
				}
			)
			("jack_connect REAPER:out1"++" "++"system:playback_1").systemCmd;
			("jack_connect REAPER:out2"++" "++"system:playback_2").systemCmd;
		}
	};

		w.view.decorator.nextLine;

	playButton = Button(w, 70 @ 20);
	playButton.states = [["play",Color.black,Color.white]];
	playButton.action = { |butt|
		~intialise.fork;
		s.sync;
		~sequencer_stream = ~sequencer.play;	
		postln("Play!");
	};
	
		w.view.decorator.nextLine;
		
	stopButton = Button(w, 70 @ 20);
	stopButton.states = [["stop",Color.black,Color.white]];
	stopButton.action = { |butt|
		~stop_all.fork;
		postln("Stop!");
	};
	
	//Bring the window to the front
	w.front;

	//When this window closes, cleanup
	w.onClose = { 
		postln("Window Closed!");
		if(~stop_all != nil, {~stop_all.fork;});
		if(~cleanup != nil, {~cleanup.fork;});
	};
)

