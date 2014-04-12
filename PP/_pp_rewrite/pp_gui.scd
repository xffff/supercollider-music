/************************************/
/* pp            	      	       	*/
/*	GUI							    */
/************************************/

(
var initButton, stopButton, playButton, routeButton, queryButton, meterButton;

GUI.qt;
~numchans = 32;	    // overall track count (fx + dry)
~numoutchans = 2;	// numver of loudspeakers
~numfxchans = 16;	// number of fx processors
~path = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/PP/_pp_rewrite";
~buffersize = 512;
~outdevice = "JackRouter";
~samplerate = 48000;

CmdPeriod.add({~stop_all.fork;});
GUI.qt;
TempoClock.default.tempo = 0.8;

postln("/****************************************************/");
postln("/* pp                                               */");
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
		("/usr/local/bin/jackd -R -P -0 -d coreaudio -r " ++
			~samplerate ++ " -p " ++ ~buffersize).unixCmd;
		3.wait;
		postln("Startup...");
		thisProcess.interpreter.executeFile(~path ++ "/pp_startup.scd");
		~startup.fork;
		~midi_init.fork;
	}
};

w.view.decorator.nextLine;

routeButton = Button(w, 70 @ 20);
routeButton.states = [["route audio",Color.black,Color.white]];
routeButton.action = { |butt|
	fork{
		postln("Routing audio...");
		for(1,2, { | i |
			// kontakt -> scsynth
			unixCmd("/usr/local/bin/jack_connect \"Kontakt 4\":out"++i++" "++"scsynth:in"++i, false);
			postln("/usr/local/bin/jack_connect \"Kontakt 4\":out"++i++" "++"scsynth:in"++i);
			0.1.wait;
			}
		);

		1.wait;

		for(1,~numoutchans, { | i |
			// connect scsynth to soundcard
			unixCmd("/usr/local/bin/jack_connect scsynth:out"++i++" "++"butt:in"++i, false);
			postln("/usr/local/bin/jack_connect scsynth:out"++i++" "++"butt:in"++i);
			0.1.wait;
			}
		);
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
		("killall \"Kontakt 4\"").unixCmd;
		0.5.wait;
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

