Event.addEventType(\ctosc, {|server|
	var freqs, lag, dur, sustain, strum;
	var bndl, oscout, hasGate, osccmd;	
	var oscEventFunctions = (
			\noteon:			#{ arg voicename, midinote=60, amp=0.1;
								[voicename, midinote, asInteger((amp * 127).clip(0, 127))] },
			\noteoff: 		#{ arg voicename, midinote=60;
								[voicename, midinote] },
			\detuned_noteon:  	#{ arg voicename, midinote=60, detune=0, amp=0.1;
								[voicename, midinote, detune, asInteger((amp * 127).clip(0, 127))] },
			\detuned_noteoff:  #{ arg voicename, midinote=60, detune=0;
								[voicename, midinote, detune] },
			\ctnote:			#{ arg voicenumber=0, noteid=0, midinote=60, amp=0.1, dur=0;
								[voicenumber, noteid, midinote, asInteger((amp * 127).clip(0, 127)), dur] },
			\ctnoteoff:  		#{ arg noteid=0;
								[noteid] },
			\program:  		#{ arg voicename, progname; 
								[ voicename, progname ] },
			\gain: 			#{ arg voicename, gain=0; // this is in dB 
								[ voicename, gain ] },
			\allNotesOff: 	#{ arg null=0; [null] },
			\kammerton: 		#{arg freq=442; [freq]}
	);
		
	freqs = ~freq = ~detunedFreq.value;

	if (freqs.isRest.not) {
		~amp = ~amp.value;
		~midinote = freqs.cpsmidi;
		strum = ~strum;
		lag = ~lag;
		sustain = ~sustain = ~sustain.value;
		oscout = ~oscout.value; // OSC address
		hasGate = ~hasGate ? true;
		osccmd = ~osccmd;
		bndl = oscEventFunctions[osccmd].valueEnvir.asCollection;

		bndl = bndl.asControlInput.flop;
				
		bndl.do {|msgArgs, i|
				var latency;

				latency = i * strum + lag;

				if(latency == 0.0) {
					oscout.sendBundle(latency, [osccmd, msgArgs].flat)
				} {
					thisThread.clock.sched(latency, {
						oscout.sendBundle(latency, [osccmd, msgArgs].flat)
					})
				};
				
				// automatically deal with noteoff messages for each note-type
				case 
					{hasGate and: { osccmd === \noteon }} {
					thisThread.clock.sched(sustain + latency, {
						oscout.sendBundle(latency, [\noteoff, msgArgs].flat)
					})}
					{hasGate and: { osccmd === \detuned_noteon }} {
					thisThread.clock.sched(sustain + latency, {
						oscout.sendBundle(latency, [\detuned_noteoff, msgArgs].flat)
					})}
					{hasGate and: { osccmd === \ctnote }} {
					thisThread.clock.sched(sustain + latency, {
						oscout.sendBundle(latency, [\ctnoteoff, msgArgs].flat)
					})};									
		};
	}
});

