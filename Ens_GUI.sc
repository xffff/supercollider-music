/************************************	*/
/* Ensemble Piece	      	       	*/
/*	GUI							*/
/************************************	*/

(
var initButton, stopButton, playButton, ctButton, routeButton, queryButton;
~path = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/Ensemble_Piece";
~contimbre_path = "/Volumes/Time Machine Backups/conTimbre";

	postln("/****************************************************/");
	postln("/* Ensemble                                         */");
	postln("/* Michael Murphy 2012                              */");
	postln("/****************************************************/");
	postln(""); postln("");

	w=Window(" ", Rect(100, 200, 85, 190));
	w.view.decorator = FlowLayout(w.view.bounds);
	w.view.decorator.gap = 10@10;
		
	initButton = Button(w, 70 @ 20);
	initButton.states = [["init",Color.black,Color.white]];
	initButton.action = { |butt|
		fork{
			postln("Starting jackd... (3s)");
			("/usr/local/bin/jackd -d coreaudio -r 48000 -p 1024").unixCmd;
			3.wait;
			postln("Startup...");
			thisProcess.interpreter.executeFile(~path ++ "/Ens_Startup.sc");
			postln("Compiled... starting scsynth...");
			~startup.fork;
			("open \"/Users/Michael_Murphy/Documents/REAPER Media/Ensemble_Piece/Ensemble_Piece_3232/Ensemble_Piece_3232.RPP\"").unixCmd;
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
				unixCmd("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"REAPER:in"++i, false);
				
				// connect MaxMSP to scsynth
				unixCmd("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"scsynth:in"++i, false);
				
				unixCmd("/usr/local/bin/jack_connect scsynth:out"++i++" "++"REAPER:in"++i, false);
				}
			);
			unixCmd("/usr/local/bin/jack_connect REAPER:out1"++" "++"system:playback_1", false);
			unixCmd("/usr/local/bin/jack_connect REAPER:out2"++" "++"system:playback_2", false);
			
			3.wait;
			postln("Ready for playback!");
		};
	};

		w.view.decorator.nextLine;

	playButton = Button(w, 70 @ 20);
	playButton.states = [["play",Color.black,Color.white]];
	playButton.action = { |butt|
		fork{	
			~initialise.fork;
			2.wait;
			~sequencer_stream = ~sequencer.play;	
		};
		postln("Play!");
	};
	
		w.view.decorator.nextLine;
		
	stopButton = Button(w, 70 @ 20);
	stopButton.states = [["stop",Color.black,Color.white]];
	stopButton.action = { |butt|
		~stop_all.fork;
		postln("Stop!");
	};
	
			w.view.decorator.nextLine;

	queryButton = Button(w, 70 @ 20);
	queryButton.states = [["query",Color.black,Color.white]];
	queryButton.action = { |butt|
		s.queryAllNodes;
	};

	
	//Bring the window to the front
	w.front;

	//When this window closes, cleanup
	w.onClose = { 
		postln("Cleaning up...");
		if(~stop_all != nil, {~stop_all.fork;});
		if(~cleanup != nil, {~cleanup.fork;});
		s.freeAll; s.quit;		
		("killall MaxMSP").unixCmd;
		("killall REAPER").unixCmd;
		("killall jackd").unixCmd;
		Environment.clear;
		postln("Bye!");
	};
)

