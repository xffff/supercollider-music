/************************************/
/* pp               			    */
/*	STARTUP				            */
/************************************/

(
~mOut = nil;
~startup = nil;
~midi_init = nil;
~initialise = nil;

~startup = {
	s = Server.local;
	o = Server.local.options;
	o.inDevice = "JackRouter";
	o.outDevice = ~outdevice;
	o.blockSize = ~buffersize;
	o.hardwareBufferSize = ~buffersize;
	o.numOutputBusChannels = 36; // ~numchans + 4 (2 stereo outboard)
	o.numInputBusChannels = 36;
	o.memSize;
	if(o.memSize<1048576,{o.memSize = 1048576}); // 1GB 2**20
	s.boot;
	if(~samplerate != s.sampleRate,
		{postln("BIG ERROR: SAMPLERATE MISMATCH...
		We are probably using the wrong audio device"); s.quit;}
	);
};

////////////////////////////////////////////////////
~midi_init = {
	MIDIClient.init;
	~mOut = MIDIOut(0);
};

////////////////////////////////////////////////////
~initialise = {
	s.waitForBoot(
		postln("STARTUP...");
		postln("________________");
		postln("Loading Synths... ");
		thisProcess.interpreter.executeFile(~path ++ "/pp_synths.scd");
		0.5.wait;
		postln("Routing Audio... ");
		thisProcess.interpreter.executeFile(~path ++ "/pp_audio.scd");
		0.5.wait;
		postln("Loading Patterns 0-7... ");
		thisProcess.interpreter.executeFile(~path ++ "/pp_patterns.scd");
		0.5.wait;
		postln("Loading Sequencer...");
		thisProcess.interpreter.executeFile(~path ++ "/pp_sequencer.scd");
	);
};
);