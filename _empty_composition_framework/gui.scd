/************************************	*/
/* Untitled Piece	         	       	*/
/*	GUI				        			*/
/************************************	*/

/********************************************************************************************

WHEN STARTING A NEW PIECE A NUMBER OF THINGS MUST BE USER CONFIGURED FIRST

0. Check your Jack config, on OSX this can be done with JackPilot.

1. Set your paths for this folder, and the conTimbre folder.

2. Set up your channel config, num dry (instruments), num fx, num loudspeakers.

3. If you wish for automatic configuration of Jack server and automatic routing between software (init/route audio buttons), the corresponding routines must be configured for your requirements.

These are found lower down in this file.  Be aware that all software must be set up to use the JackRouter as its host, or it won't work.

The default audio routing setup for this system is connecting: MSP -> REAPER; MSP -> SC; SC -> REAPER
SC communicates with conTimbre using OSC over port 3000.

RESULT=0 = The operation was successful.
RESULT=1 = The operation was unsuccessful, check your settings.

Always check routing has been successful with qjackctl or similar.

4. Location of different elements:

SynthDefs go in ./synths.scd (fx)
Buffers go in ./audio.scd (recording buffers)
Patterns go in ./patterns.scd (the meat and potatoes)
Sequencer lives in ./sequencer.scd (macrostructure)
Mixer lives in ./mixer.scd (voice specific output / dB / panning)

If you run out of space for patterns, or wish to break it up - make another patterns file.
These can be executed in series by modifying the startup procedure in ./startup.scd

*********************************************************************************************/
(
var initButton, stopButton, playButton, ctButton, routeButton, queryButton, meterButton;

// THESE MUST BE SET
~path = "/Users/Michael_Murphy/Documents/SuperCollider/Mieks/_empty_composition_framework/";
~contimbre_path = "/Volumes/BACKUP_WD/20130103_conTimbre1.1/";

// PIECE DEPENDANT
~numoutchans = 2;	// numver of loudspeakers
~numdrychans = 4;  // number of dry chans (instruments)
~numfxchans = 16;	// number of fx processors
~numchans = ~numdrychans+~numfxchans;

// MODIFY THIS IF LATENCY OR DROPOUTS OCCUR
~buffersize = 512;

// USING A DAW?
// true = each output goes to a single channel
// false = output is mixed in SC and goes out to loudspeakers directly
~dawversion = true;

CmdPeriod.add({~stop_all.fork;});

postln("/****************************************************/");
postln("/* Untitled_Piece                                   */");
postln("/* Michael Murphy 2013                              */");
postln("/****************************************************/");
postln(""); postln("");

// CLOCKFACE QUARK REQ'D FOR THIS
~clock = ClockFace.new;
w=Window(" ", Rect(100, 200, 85, 210));
w.view.decorator = FlowLayout(w.view.bounds);
w.view.decorator.gap = 10@10;

initButton = Button(w, 70 @ 20);
initButton.states = [["init",Color.black,Color.white]];
initButton.action = { |butt|
	fork{
		postln("Starting jackd... (3s)");

		// CHANGE THIS TO SUIT YOUR SOUND CARD
		("/usr/local/bin/jackd -R -P -0 -d coreaudio -d YamahaFWAudioEngine:0 -i 12 -o 12 -r 48000 -p "++~buffersize).unixCmd;
		//("/usr/local/bin/jackd -R -P 0 -d coreaudio -r 48000 -p "++~buffersize).unixCmd;
		3.wait;
		postln("Startup...");
		thisProcess.interpreter.executeFile(~path ++ "/startup.scd");
		postln("Compiled... starting scsynth...");
		~startup.fork;
	};
	s.waitForBoot({s.makeGui;});
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
		// IF YOU WOULD LIKE TO UTILISE AUTOMATIC ROUTING, MODIFY THIS ROUTINE TO SUIT YOUR REQS

		postln("Routing audio...");
		if(~dawversion==true,{
			for(1,~numdrychans, { | i |
				// first connect MaxMSP to scsynth
				unixCmd("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"scsynth:in"++i, false);
				postln("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"scsynth:in"++i);
				0.1.wait;
				}
			);

			1.wait;

			for(1,~numchans, { | i |
				// connect scsynth to REAPER
				unixCmd("/usr/local/bin/jack_connect scsynth:out"++i++" "++"REAPER:in"++i, false);
				postln("/usr/local/bin/jack_connect scsynth:out"++i++" "++"REAPER:in"++i);
				0.1.wait;
				}
			);

			1.wait;

			for(1,~numoutchans, { | i |
				// connect REAPER to output
				unixCmd("/usr/local/bin/jack_connect REAPER:out"++i++" "++"system:playback_"++i, false);
				postln("/usr/local/bin/jack_connect REAPER:out"++i++" "++"system:playback_"++i);
				0.1.wait;
				}
			);
			},{
				for(1,~numdrychans, { | i |
					// first connect MaxMSP to scsynth
					unixCmd("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"scsynth:in"++i, false);
					postln("/usr/local/bin/jack_connect MaxMSP:out"++i++" "++"scsynth:in"++i);
					0.1.wait;
					}
				);

				1.wait;

				for(1,~numoutchans, { | i |
					// connect scsynth to output
					unixCmd("/usr/local/bin/jack_connect scsynth:out"++i++" "++"system:playback_"++i, false);
					postln("/usr/local/bin/jack_connect scsynth:out"++i++" "++"system:playback_"++i);
					0.1.wait;
					}
				);
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
		//("killall MaxMSP").unixCmd;
		//0.5.wait;
		//("killall REAPER").unixCmd;
		//0.5.wait;
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

