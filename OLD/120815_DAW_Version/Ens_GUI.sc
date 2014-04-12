/************************************	*/
/* Ensemble Piece	      	       	*/
/*	GUI							*/
/************************************	*/

(
var initButton, stopButton, playButton, ctButton, routeButton, queryButton, meterButton;GUI.qt;
~path = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/Ensemble_Piece";
~contimbre_path = "/Volumes/Time Machine Backups/conTimbre";
~buffersize = 2048;
CmdPeriod.add({~stop_all.fork;});
GUI.qt;

	postln("/****************************************************/");
	postln("/* Ensemble                                         */");
	postln("/* Michael Murphy 2012                              */");
	postln("/****************************************************/");
	postln(""); postln("");

	~clock = ClockFace.new;
	w=Window(" ", Rect(100, 200, 85, 210));
	w.view.decorator = FlowLayout(w.view.bounds);
	w.view.decorator.gap = 10@10;
		
	initButton = Button(w, 70 @ 20);
	initButton.states = [["init",Color.black,Color.white]];
	initButton.action = { |butt|
		fork{
			postln("Starting jackd... (3s)");
			//("/usr/local/bin/jackd -R -P -0 -d coreaudio -d YamahaFWAudioEngine:0 -i 12 -o 12 -r 48000 -p "++~buffersize).unixCmd;
			("/usr/local/bin/jackd -R -P 0 -d coreaudio -r 48000 -p "++~buffersize).unixCmd;
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
			for(1,16, { | i | 
				// first connect MaxMSP to scsynth and Reaper
				unixCmd("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"REAPER:in"++i, false);
				0.1.wait;
				unixCmd("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"scsynth:in"++i, false);
				0.1.wait;
				}
			);
			for(1,32, { | i | 
				// connect scsynth to reaper 1-32
				unixCmd("/usr/local/bin/jack_connect scsynth:out"++i++" "++"REAPER:in"++i, false);
				0.1.wait;
				}
			);

			1.wait;
			
//			for(1,12, { | i | 
//				// connect reaper to soundcard
//				unixCmd("/usr/local/bin/jack_connect REAPER:out"++i++" "++"system:playback_"++i, false);
//				0.1.wait;
//				}
//			);
//			
//			for(9,10, { | i | 
//				// connect reverb to reaper
//				unixCmd("/usr/local/bin/jack_connect system:capture_"++i++" "++"REAPER:in"++(i+24), false);
//				0.1.wait;
//				}
//			);
			
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
			5.wait;
			~sequencer_stream = ~sequencer.play;	
			~clock.cursecs_(0);
			~clock.play;
		};
		postln("Play!");
	};
	
		w.view.decorator.nextLine;
		
	stopButton = Button(w, 70 @ 20);
	stopButton.states = [["stop",Color.black,Color.white]];
	stopButton.action = { |butt|
		~stop_all.fork;
		s.freeAll;
		~clock.stop;
		postln("Stop!");
	};
	
			w.view.decorator.nextLine;

	queryButton = Button(w, 70 @ 20);
	queryButton.states = [["tree",Color.black,Color.white]];
	queryButton.action = { |butt|
		s.plotTree(0.5);
	};

			w.view.decorator.nextLine;

	meterButton = Button(w, 70 @ 20);
	meterButton.states = [["meter",Color.black,Color.white]];
	meterButton.action = { |butt|
		s.meter;
	};

	//Bring the window to the front
	w.front;

	//When this window closes, cleanup
	w.onClose = { 
		fork{
			postln("Cleaning up...");
			if(~stop_all != nil, {~stop_all.fork;});
			if(~cleanup != nil, {~cleanup.fork;});
			s.freeAll; 		
			("killall MaxMSP").unixCmd;
			0.5.wait;
//			("killall REAPER").unixCmd;
//			0.5.wait;
			("killall jackd").unixCmd;
			0.5.wait;
			("killall scsynth").unixCmd;
			0.5.wait;
			~clock.free;
			Environment.clear;
			postln("Bye!");
		}
	};
)

